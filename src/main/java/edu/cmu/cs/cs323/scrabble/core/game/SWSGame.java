package edu.cmu.cs.cs323.scrabble.core.game;

import edu.cmu.cs.cs323.scrabble.core.board.Board;
import edu.cmu.cs.cs323.scrabble.core.board.DefaultBoard;
import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;
import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.board.square.ImmutableSquare;
import edu.cmu.cs.cs323.scrabble.core.letters.DefaultTileBag;
import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.letters.TileBag;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTileFactory;
import edu.cmu.cs.cs323.scrabble.core.specialtile.store.SpecialTileStore;
import edu.cmu.cs.cs323.scrabble.core.specialtile.defaults.DefaultSpecialTileFactories;
import edu.cmu.cs.cs323.scrabble.core.specialtile.store.SpecialTileStoreBuilder;
import edu.cmu.cs.cs323.scrabble.core.validation.DummyValidator;
import edu.cmu.cs.cs323.scrabble.core.validation.TextFileValidator;
import edu.cmu.cs.cs323.scrabble.core.validation.WordValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a game of Scrabble With Stuff (SWS).
 * 
 * Responsibilities include:
 * 
 * <pre>
 *   - Maintaining the list of players
 *   - Controlling the turn cycle
 *   - Updating player scores
 *   - Updating the game board with player actions
 * </pre>
 */
public final class SWSGame {
  public static final int PLAYER_TILE_LIMIT = 7;
  private static final Map<SpecialTileFactory,Integer> TILE_PRICES = defaultTilePrices();
  private static final String ERROR_PLACE_MOVE = "Could not place move.";
  private static final String ERROR_PLACE_SPECIAL = "Could not place special tile.";
  private static final String ERROR_DOUBLE_SPECIAL =
          "Special tiles can not be placed on top of each other.";
  private static final String ERROR_BUY_SPECIAL = "Could not buy special tile.";
  private static final String ERROR_SWAP = "Could not swap tiles.";
  private static final String ERROR_CAN_CHALLENGE = "Could not challenge tiles.";
  private static final String ERROR_INVALID_POSITION = "Not a valid position.";
  private boolean firstMoveTaken = false;

  /**
   * Maps tile factories (what the store uses to create tiles that people purchase) to a price.
   */
  private static Map<SpecialTileFactory,Integer> defaultTilePrices() {
    Map<SpecialTileFactory,Integer> tilePrices = new HashMap<>();
    tilePrices.put(DefaultSpecialTileFactories.NEGATIVE_POINTS, 20);
    tilePrices.put(DefaultSpecialTileFactories.REVERSE, 10);
    tilePrices.put(DefaultSpecialTileFactories.BOOM, 18);
    tilePrices.put(DefaultSpecialTileFactories.STEAL_SCORE, 25);
    tilePrices.put(DefaultSpecialTileFactories.SKIP_TURN, 15);
    return tilePrices;
  }

  private final SpecialTileStore tileStore;
  private final TileBag tileBag = DefaultTileBag.create();
  private final Board board = DefaultBoard.create();
  private final WordValidator validator;
  private final List<Player> players;
  private final List<Player> skippedPlayers = new ArrayList<>();
  private final List<SWSGameListener> gameListeners = new ArrayList<>();
  private final Position centerPosition;
  private int playerIndex = 0;
  private Turn turn;

  /**
   * Direction of motion through the players.
   *
   * I would usually consider these fields totally private, but they have accessors or mutators
   * for SpecialTile functionality. */
  public enum TurnDirection {
    CW {
      @Override public TurnDirection opposite() { return CCW; }
    }, CCW {
      @Override public TurnDirection opposite() { return CW; }
    };

    /**
     * @return The direction opposite this one.
     */
    public TurnDirection opposite() { return null; }
  }
  private TurnDirection turnDirection = TurnDirection.CW;

  /**
   * Initiates a game for a given list of players.
   *
   * @param players list of players involved in the game.
   */
  public SWSGame(List<Player> players) {
    this.players = new ArrayList<>(players);
    this.validator = new TextFileValidator();

    /* Register the default "Special Tiles" with the tile store. */
    SpecialTileStoreBuilder builder = new SpecialTileStoreBuilder();
    for (SpecialTileFactory defaultFactory : DefaultSpecialTileFactories.values()) {
      builder.registerSpecialTileFactory(defaultFactory, TILE_PRICES.get(defaultFactory));
    }
    tileStore = builder.construct();

    /* Store the center square, which is of special importance because the first move is only valid
     * if it crosses the center square. */
    int centerRow = DefaultBoard.HEIGHT / 2 - 1 + DefaultBoard.HEIGHT % 2;
    int centerCol = DefaultBoard.WIDTH / 2 - 1 + DefaultBoard.WIDTH % 2;
    this.centerPosition = new Position(centerRow, centerCol);
  }

  /**
   * Returns the player who is taking it his or her turn.
   * 
   * @return The player taking his or her turn.
   */
  public Player currentPlayer() {
    return turn.player();
  }

  /**
   * Attempts to play the given move on the board.
   *
   * @param player
   * Player placing tiles.
   * @param move
   *  Move representing what tiles to place and where to place them.
   */
  public void placeMove(Player player, Move move) {
    if (turn.canTakeLetterAction(player)
            && board.isValidMove(player, move, !firstMoveTaken)
            && firstMoveIsCorrect(move)) {
      turn.takeLetterAction(player);
      notifyTurnChanged();
      for (Position position : move.placements().keySet()) {
        List<SpecialTile> specialTiles = board.pickupSpecialTiles(position);
        for (SpecialTile specialTile : specialTiles) {
          turn.addTriggeredSpecialTile(position, specialTile);
        }
        LetterTile letterTile = move.placements().get(position);
        player.letterTiles().remove(letterTile);
        board.placeLetterTile(position, letterTile);
        notifySquareChanged(position);
      }
      player.setScore(player.getScore() + move.totalScore(board));
      notifyPlayerChanged(player);
      turn.setPlayedMove(move);
      notifyTurnChanged();
    } else {
      notifyErrorOccured(player, ERROR_PLACE_MOVE);
    }
  }

  /**
   * Places a player's special tile on to the given point on the board.
   *
   * @param player Player placing tiles
   * @param position Position where tile should be placed.
   * @param specialTile Special Tile to be placed.
   */
  public void placeSpecialTile(Player player, Position position, SpecialTile specialTile) {
    if (currentPlayer().equals(player)
            && player.specialTiles().contains(specialTile)
            && turn.canTakeSpecialAction(player)) {
      if (viewOfSquare(player, position).hasSpecialTiles()) {
        notifyErrorOccured(player, ERROR_DOUBLE_SPECIAL);
      } else {
        turn.takeSpecialAction(player);
        notifyTurnChanged();
        player.specialTiles().remove(specialTile);
        notifyPlayerChanged(player);
        board.placeSpecialTile(position, specialTile);
        notifySquareChanged(position);
      }
    } else {
      notifyErrorOccured(player, ERROR_PLACE_SPECIAL);
    }
  }

  /**
   * Purchases a special tile from the tile store for the player.
   *
   * @param player player purchasing tile.
   * @param specialTileIdentifier Identifier for special tile to be purchased.
   */
  public void buySpecialTile(Player player, String specialTileIdentifier) {
    if (tileStore.hasSpecialTile(specialTileIdentifier)) {
      turn.takeSpecialAction(player);
      notifyTurnChanged();
      tileStore.buySpecialTile(player, specialTileIdentifier);
      notifyPlayerChanged(player);
    } else {
      notifyErrorOccured(player, ERROR_BUY_SPECIAL);
    }
  }

  /**
   * Swaps the given tiles with tiles from the bag.
   *
   * @param player player whose tiles to swap.
   * @param oldTiles tiles to be swapped.
   */
  public void swapTiles(Player player, List<LetterTile> oldTiles) {
    if (currentPlayer().equals(player)
            && oldTiles.size() > 0
            && player.letterTiles().containsAll(oldTiles)
            && tileBag.canSwapTiles()) {
      turn.takeLetterAction(player);
      notifyTurnChanged();
      player.letterTiles().removeAll(oldTiles);
      List<LetterTile> newTiles = tileBag.swapTiles(oldTiles);
      player.letterTiles().addAll(newTiles);
      notifyPlayerChanged(player);
    } else {
      notifyErrorOccured(player, ERROR_SWAP);
    }
  }

  /**
   * @param player  Player trying to swap tiles.
   * @return true if tiles can be swapped, false otherwise.
   */
  public boolean canSwapTiles(Player player) {
    return player.equals(currentPlayer())
            && tileBag.canSwapTiles()
            && turn.canTakeLetterAction(player);
  }

  /**
   * Challenges the last tile placement made on the board. If the challenge succeeds, the player who
   * played his or her tiles gets them back, and their score is deducted by the number of points
   * the words were worth. If the challenge fails, the challenger loses their turn.
   *
   * The Challenge only occurs if it is allowed at the current time, which can be checked with the
   * {@code canTakeChallengeAction(challenger)} method of the {@code Turn} class.
   *
   * @param challenger Player who instigated the challenge.
   */
  public void challengeLastMove(Player challenger) {
    if (turn.canTakeChallengeAction(challenger)) {
      turn.takeChallengeAction(challenger);
      notifyTurnChanged();
      List<PlayedWord> words = lastMove().words(board);
      if (!allWordsValid(words)) {
        /* One or more words were invalid, meaning the current player's tiles must be returned
         * to them and their points are rescinded. */
        Player currentPlayer = currentPlayer();
        currentPlayer.setScore(currentPlayer.getScore() - lastMove().totalScore(board));
        Map<Position, LetterTile> placements = lastMove().placements();
        for (Position position : placements.keySet()) {
          LetterTile tile = board.pickupLetterTile(position);
          notifySquareChanged(position);
          currentPlayer.letterTiles().add(tile);
        }
        notifyPlayerChanged(currentPlayer);
        /* Also, ensure that the special tiles are back where they belong. */
        Map<Position,List<SpecialTile>> allSpecialTiles = turn.triggeredSpecialTiles();
        turn.clearTriggeredSpecialTiles();
        turn.setPlayedMove(null);
        notifyTurnChanged();
        for (Position position : allSpecialTiles.keySet()) {
          for (SpecialTile specialTile : allSpecialTiles.get(position)) {
            board.placeSpecialTile(position, specialTile);
            notifySquareChanged(position);
          }
        }
      } else {
        /* All words were valid, meaning the challenger must lose their turn. */
        skipTurn(challenger);
      }
    } else {
      notifyErrorOccured(challenger, ERROR_CAN_CHALLENGE);
    }
  }

  /**
   * Finalizes the current player's turn. This includes drawing the tiles for that player, which is traditionally
   * the action that signifies the end of a turn.
   *
   * @param player player trying to end turn.
   */
  public void endTurn(Player player) {
    if (turn.canTakeEndAction(player)) {
    /* Ensure that the current turn locks out any players from taking action. */
      turn.takeEndAction(player);

    /* Before any tiles are potentially removed by special tiles, but after a challenge may have
     * removed them, record whether or not the move was the first move and covered the center tile.
     */
      if (!firstMoveTaken && turn.getPlayedMove() != null) {
        assert(board.getLetterTile(centerPosition) != null);
        firstMoveTaken = true;
      }

    /* If special tiles were triggered, perform their actions. */
      Map<Position,List<SpecialTile>> allSpecialTiles = turn.triggeredSpecialTiles();
      for (Position position : allSpecialTiles.keySet()) {
        for (SpecialTile specialTile : allSpecialTiles.get(position)) {
          specialTile.performAction(this, board, position, gameListeners);
        }
      }
      drawTilesForPlayer(currentPlayer());
      if (isGameOver()) {
        notifyGameEnded(highestScoringPlayer());
      } else {
        advancePlayer();
      }
    }
  }

  /**
   * Retrieves tile store being used for this game.
   *
   * @return `tileStore` object associated with this game.
   */
  public SpecialTileStore tileStore() {
    return tileStore;
  }

  /**
   * Performs all setup operations for the game. This method MUST be called before any actions are
   * taken, and can only be called once.
   */
  public void setupGame() {
    for (Player player : players) {
      drawTilesForPlayer(player);
    }
    playerIndex = -1; // So the "next" index is 0
    advancePlayer();
  }

  /**
   * Reverses the order that players go in.
   *
   * This method exists solely for the ReverseOrderSpecialTile, otherwise it is not necessary.
   */
  public void reverseTurnDirection() {
    turnDirection = turnDirection.opposite();
  }

  /**
   * @return The last move placed on the board.
   */
  public Move lastMove() {
    return turn.getPlayedMove();
  }

  /**
   * Tells the game to skip a turn for the given player. Only public for the SkipTurn Special Tile.
   *
   * @param playerToSkip Player who recieves a skipped turn penalty.
   */
  public void skipTurn(Player playerToSkip) {
    skippedPlayers.add(playerToSkip);
  }

  /* Listener Methods */

  /**
   * Adds a new SWSGameListener to the list of objects listening to the events occurring in this
   * game.
   *
   * @param newListener SWSGameListener that needs to listen to events occurring in this game.
   */
  public void addSWSGameListener(SWSGameListener newListener) {
    gameListeners.add(newListener);
  }

  /**
   * Removes a new SWSGameListener from the list of objects listening to the events occurring in
   * this game.
   *
   * @param oldListener SWSGameListener that needs to listen to events occurring in this game.
   */
  public void removeSWSGameListener(SWSGameListener oldListener) {
    gameListeners.remove(oldListener);
  }

  /**
   * Returns an immutable view of the square at the {@code position}, from the perspective of
   * {@code player}. This means that the view of the square is filtered so that it only has the
   * components that the player could see.
   *
   * Requires {@code position} is a valid position on the game board.
   *
   * @param player Player viewing square.
   * @param position Position of square.
   * @return A view of the square as it would be seen by {@code player}.
   */
  public ImmutableSquare viewOfSquare(Player player, Position position) {
    if (!board.isValidPosition(position)) {
      throw new IllegalArgumentException(ERROR_INVALID_POSITION);
    }
    ImmutableSquare original = board.viewOfSquare(position);
    List<SpecialTile> filtered = original.getSpecialTiles();
    filtered.removeIf((SpecialTile st) -> !st.owner().equals(player));
    return new ImmutableSquare(original.wordMultiplier(), original.letterMultiplier(),
            original.getLetterTile(), filtered);
  }

  /** @return true if the end conditions are met, false otherwise. */
  private boolean isGameOver() {
    return tileBag.isEmpty();
  }

  /** @return the highest scoring player in the game. */
  private Player highestScoringPlayer() {
    Player winner = players.get(0);
    for (Player player : players) {
      if (winner.getScore() < player.getScore()) {
        winner = player;
      }
    }
    return winner;
  }

  /**
   * @param move Move being placed.
   * @return true if the move is not the first move, or if it is the first move and properly covers
   * the center position; false otherwise.
   */
  private boolean firstMoveIsCorrect(Move move) {
    return firstMoveTaken || move.placements().keySet().contains(centerPosition);
  }

  private void notifySquareChanged(Position position) {
    for (SWSGameListener listener : gameListeners) {
      listener.squareChanged(position);
    }
  }

  private void notifyPlayerChanged(Player player) {
    for (SWSGameListener listener : gameListeners) {
      listener.playerChanged(player);
    }
  }

  private void notifyTurnChanged() {
    for (SWSGameListener listener : gameListeners) {
      listener.turnChanged(turn);
    }
  }

  private void notifyGameEnded(Player winner) {
    for (SWSGameListener listener : gameListeners) {
      listener.gameEnded(winner);
    }
  }

  private void notifyErrorOccured(Player target, String message) {
    // the method i needed wasn't implemented
    // we should probably fix this at some point
  }

  /* Private Methods */

  private void drawTilesForPlayer(Player player) {
    while (!tileBag.isEmpty() && player.letterTiles().size() < PLAYER_TILE_LIMIT) {
      LetterTile letterTile = tileBag.drawTile();
      player.letterTiles().add(letterTile);
    }
    notifyPlayerChanged(player);
  }

  private void advancePlayer() {
    /* Calculates the real modulus of player index and the number of players. Java usually computes
     * remainder, which can result in negative values, and therefore ArrayIndexOutOfBounds. */
    int delta = turnDirection == TurnDirection.CW ? 1:-1;
    playerIndex = ((playerIndex + delta) % players.size() + players.size()) % players.size();
    if (skippedPlayers.contains(players.get(playerIndex))) {
      skippedPlayers.remove(players.get(playerIndex));
      advancePlayer();
    } else {
      turn = new Turn(players.get(playerIndex));
      notifyTurnChanged();
    }
  }

  private  boolean allWordsValid(List<PlayedWord> words) {
    for (PlayedWord word : words) {
      if (!validator.isValidWord(word)) {
        return false;
      }
    }
    return true;
  }

}
