package edu.cmu.cs.cs323.scrabble.core.specialtile.defaults;

import edu.cmu.cs.cs323.scrabble.core.board.Board;
import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;
import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Move;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGameListener;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Special Tile that goes "BOOM" and blows up all tiles within a 3 tile radius.
 */
final class BoomSpecialTile implements SpecialTile {
  private static final int RADIUS = 3;

  private Player owner;

  BoomSpecialTile(Player owner) {
    this.owner = owner;
  }

  public static String identifier() {
    return "Boom";
  }

  /**
   * Obliterates all surrounding words by removing letter tiles within a three square radius.
   *
   * @param game
   *          Instance of the current SWS game.
   * @param board
   *          Game board.
   * @param position
   *          Position the special tile was in when triggered.
   * @param listeners
   *          SWSGameListeners associated with the current game.
   */
  @Override
  public void performAction(SWSGame game, Board board, Position position,
                            List<SWSGameListener> listeners) {
    /*
     * The upper and lower or right and left edges of the move will be needed later when we
     * recalculate the score.
     */
    Move move = game.lastMove();
    Position upperLeft = move.upperLeft();
    Position lowerRight = move.lowerRight();
    int originalScore = move.totalScore(board);

    /*
     * This section traverses all positions within 3 squares of the trigger position, and removes
     * any letter tiles or special tiles it finds.
     */
    int startRow = Integer.max(position.row - RADIUS, 0);
    int startCol = Integer.max(position.col - RADIUS, 0);
    int stopRow = position.row + RADIUS + 1;
    int stopCol = position.col + RADIUS + 1;
    Set<Position> removedPositions = new HashSet<>();
    for (int i = 0; startRow + i < stopRow; i++) {
      for (int j = 0; startCol + j < stopCol; j++) {
        Position radialPosition = new Position(startRow + i, startCol + j);
        removedPositions.add(radialPosition);
        if (board.isValidPosition(radialPosition)) {
          board.pickupLetterTile(radialPosition);
          board.pickupSpecialTiles(radialPosition);
          listeners.forEach(listener -> listener.squareChanged(radialPosition));
        }
      }
    }

    /*
     * This is a hacky, but effective way to re-calculate the score that should be given to the
     * original player. The basic idea is that after the BOOM, you can use the original left<->right
     * or upper<->lower positions of the word to get the words formed by the non-removed tiles.
     *
     * Case 1: Neither was removed
     *    This implies that only the center of the word was removed. By searching for a word from
     *    each end, we can get the two remaining word fragments.
     *
     * Case 2: WLOG First was removed, Last remained
     *    In this case, the only remaining fragments form a word connected to Last, so we only
     *    need to find words from Last.
     *
     * Case 3: Both were removed
     *    All placed tiles are gone, no score at all.
     */
    int newScore = 0;
    List<PlayedWord> words = new ArrayList<>();
    switch (move.orientation()) {
      case ACROSS:
        if (!removedPositions.contains(upperLeft)) {
          PlayedWord firstChunk = board.horizontalWordIncluding(upperLeft);
          if (firstChunk != null) {
            newScore += firstChunk.score();
          }
        }
        if (!removedPositions.contains(lowerRight)) {
          PlayedWord lastChunk = board.horizontalWordIncluding(lowerRight);
          if (lastChunk != null) {
            newScore += lastChunk.score();
          }
        }
        for (Position possibleTilePos : move.placements().keySet()) {
          if (!removedPositions.contains(possibleTilePos)) {
            PlayedWord tempWord = board.verticalWordIncluding(possibleTilePos);
            if (tempWord != null) {
              newScore += tempWord.score();
            }
          }
        }
        break;
      case DOWN:
        if (!removedPositions.contains(upperLeft)) {
          PlayedWord firstChunk = board.verticalWordIncluding(upperLeft);
          if (firstChunk != null) {
            newScore += firstChunk.score();
          }
        }
        if (!removedPositions.contains(lowerRight)) {
          PlayedWord lastChunk = board.verticalWordIncluding(lowerRight);
          if (lastChunk != null) {
            newScore += lastChunk.score();
          }
        }
        for (Position possibleTilePos : move.placements().keySet()) {
          if (!removedPositions.contains(possibleTilePos)) {
            PlayedWord tempWord = board.horizontalWordIncluding(possibleTilePos);
            if (tempWord != null) {
              newScore += tempWord.score();
            }
          }
        }
        break;
      default:
      /* No default is needed, because we have a case for each enum, but checkstyle complains. */
        break;
    }

    /* To update the score, subtract the original score from the player's score, and add back the
     * new score.
     */
    Player currentPlayer = game.currentPlayer();
    currentPlayer.setScore(currentPlayer.getScore() - originalScore + newScore);
    listeners.forEach(listener -> listener.playerChanged(currentPlayer));
  }

  @Override
  public Player owner() {
    return owner;
  }

  @Override
  public String toString() {
    return identifier();
  }
}
