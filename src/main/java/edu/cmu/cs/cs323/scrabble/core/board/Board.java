package edu.cmu.cs.cs323.scrabble.core.board;

import edu.cmu.cs.cs323.scrabble.core.board.square.ImmutableSquare;
import edu.cmu.cs.cs323.scrabble.core.board.square.Square;
import edu.cmu.cs.cs323.scrabble.core.board.square.StandardSquare;
import edu.cmu.cs.cs323.scrabble.core.game.Move;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.letters.Letter;
import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.*;

/**
 * ScrabbleWithStuff Board object. Tracks letter tile and special tile placements.
 */
public final class Board {
  private final int width;
  private final int height;
  private final Map<Position, Square> squares;

  /**
   * Factory for creating a blank board (board with no special squares) of a certain size.
   *
   * @param width Desired width
   * @param height Desired height
   * @return A blank board with the desired width and height.
   */
  public static Board blank(int width, int height) {
    Map<Position,Square> squareMap = new HashMap<>();
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        squareMap.put(new Position(r, c), new StandardSquare());
      }
    }
    return new Board(width, height, squareMap);
  }

  /**
   * Creates a board with the given mapping of squares.
   *
   * Requires that for all rows and columns within the bounds of `width` and `height`, there is a square at the corresponding position.
   *
   * @param width width of the baord
   * @param height height of the board
   * @param squares Mapping of positions to square objects.
     */
  Board(int width, int height, Map<Position, Square> squares) {
    this.width = width;
    this.height = height;
    this.squares = squares;
  }

  /**
   * Determines whether the given position is a valid board position.
   * Specifically, the {@code row} and {@code col} fields of the position must
   * be non-negative, and less than the height and width of the board,
   * respectively.
   *
   * @param position
   *          Position in question.
   * @return True if the position exists on the given board.
   */
  public boolean isValidPosition(Position position) {
    return squares.containsKey(position);
  }
  
  /**
   * Determines whether the placements specified by the given move are valid for
   * the current board state.
   *
   * @param player
   *  Player placing the move.
   * @param move
   *          Move in question.
   * @return True if the move can be legally performed on the current board,
   *         false otherwise.
   */
  public boolean isValidMove(Player player, Move move, boolean isFirstMove) {
    Map<Position, LetterTile> placements = move.placements();
    List<Position> placementPositions = new ArrayList<>(placements.keySet());
    /* Ensure all positions are on the board, and no tiles are being placed where tiles already
     * exist. If there is a letter tile in a position, it is definitely blocked, and players are
     * blocked from hitting their own special tiles.*/

    /* Ensure player has the tiles they want to place */
    if (!player.letterTiles().containsAll(placements.values())) {
      return false;
    }

    /*
     * Ensure the positions are in a valid "across" or "down" configuration.
     *
     * Also note that because the position list comes from a set, there are no duplicate positions. This means that it
     * would be impossible for rows and cols to _both_ all be equal, unless there was only one tile, in which case it
     * is acceptable. Therefore (rowsAllEqual || colsAllEqual) is sufficient to say all tiles are laid out across or
     * all tiles are laid out down.
     */
    boolean rowsAllEqual = Position.rowsAllEqual(placementPositions);
    boolean colsAllEqual = Position.colsAllEqual(placementPositions);
    if (!(rowsAllEqual || colsAllEqual)) {
      return false;
    }

    /*
    * Ensure the combination of new and existing tiles forms a contiguous sequence.
    */
    Direction placementDirection = rowsAllEqual ? Direction.RIGHT:Direction.DOWN;
    Position position = move.upperLeft();
    final Position stopPosition = nextPosition(move.lowerRight(), placementDirection);
    while (position != null && !position.equals(stopPosition)) {
        if (!(squares.get(position).hasLetterTile() || placementPositions.contains(position))) {
          return false;
        }
        position = nextPosition(position, placementDirection);
    }

    /* Ensure the move is not in the middle of space. */
    if (isFirstMove) {
      /* If it is the first move, it has to be in the middle of space, but we need to make sure that
       * it is more than one letter. */
      return !move.upperLeft().equals(move.lowerRight());
    } else {
      /* Otherwise, ensure the move is connected to existing tiles, not in the middle of space. */
      return hasAdjacentLetter(move.upperLeft()) || hasAdjacentLetter(move.lowerRight());
    }
  }

  /**
   * Places a letter tile at the given position.
   *
   * @param position
   * Position to place tile at.
   * @param letterTile
   * Letter tile to be put in position.
   */
  public void placeLetterTile(Position position, LetterTile letterTile) {
    if (!isValidPosition(position) || squares.get(position).hasLetterTile()) {
      throw new IllegalArgumentException("Invalid tile placement");
    }
    squares.get(position).placeLetterTile(letterTile);
  }

  /**
   * Places a special tile at the given position.
   *
   * Requires:
   *   - {@code position} is a valid position on the board
   *   - There are no letter tiles at {@code position}
   *   - No special tile belonging to the owner of {@code specialTile} is at {@code position}
   *
   * @param position
   * Position to place tile at.
   * @param specialTile
   * Special tile to be put in position.
   */
  public void placeSpecialTile(Position position, SpecialTile specialTile) {
    if (!isValidPosition(position) || squares.get(position).hasLetterTile()) {
      throw new IllegalArgumentException("Invalid tile placement");
    }
    for (SpecialTile existingTile : squares.get(position).getSpecialTiles()) {
      if (existingTile.owner().equals(specialTile.owner())) {
        throw new IllegalArgumentException("Cannot combine special tiles.");
      }
    }
    squares.get(position).placeSpecialTile(specialTile);
  }

  /**
   * Gets the longest horizontal word that contains the given position.
   *
   * Requires {@code isValidPosition(position)}, and that there is a letter tile on the given position.
   *
   * @param position
   * Position that must be part of the horizontal word returned.
   *
   * @return
   * Horizontal word beginning at or before and ending at or after the given position. If no such word exists, such as
   * when this tile is the only tile forming that word (one letter words are not considered words in Scrabble), null is
   * returned.
   */
  public PlayedWord horizontalWordIncluding(Position position) {
    return getContinuousWord(position, Direction.LEFT, Direction.RIGHT);
  }

  /**
   * Gets the longest verical word that contains the given position.
   *
   * Requires there is a letter tile on the given position.
   *
   * @param position
   * Position that must be part of the vertical word returned.
   *
   * @return
   * Vertical word beginning at or above and ending at or below the given position. If no such word exists, such as when
   * this tile is the only tile forming that word (one letter words are not considered words in Scrabble), null is returned.
   */
  public PlayedWord verticalWordIncluding(Position position) {
    return getContinuousWord(position, Direction.UP, Direction.DOWN);
  }

  /**
   * Picks up the letter tile at the given position.
   *
   * @param position
   * Position to place tile at.
   * @return
   * The letter tile in position.
   */
  public LetterTile pickupLetterTile(Position position) {
    if (!isValidPosition(position)) {
      throw new IllegalArgumentException("Invalid tile placement");
    }

    return squares.get(position).removeLetterTile();
  }

  /**
   * Returns a (possibly empty) list of the special tiles present at position p,
   * then clears those special tiles from the board (hence `pickup` rather than
   * `get`).
   *
   * Requires {@code isValidPosition(position)}
   * 
   * @param position
   *          Position of tiles to be picked up.
   * @return A list of special tiles from position p.
   */
  public List<SpecialTile> pickupSpecialTiles(Position position) {
    if (!isValidPosition(position)) {
      throw new IllegalArgumentException("Invalid position argument.");
    }

    Square square = squares.get(position);

    List<SpecialTile> specialTiles = square.getSpecialTiles();
    square.clearSpecialTiles();

    return specialTiles;
  }

  /**
   * Returns a the letter tile at the given position.
   *
   * Requires {@code isValidPosition(position)}
   *
   * @param position
   *          Position of letter tile.
   * @return The letter tile at {@code position} if it exists, {@code null} otherwise.
   */
  public LetterTile getLetterTile(Position position) {
    if (!isValidPosition(position)) {
      throw new IllegalArgumentException("Invalid position argument.");
    }

    Square square = squares.get(position);
    return square.getLetterTile();
  }

  /**
   * Returns an immutable copy of the square at the given position.
   *
   * Requires {@code isValidPosition(position)}
   *
   * @param position Position of square to grab
   * @return immutable copy of square at position.
   */
  public ImmutableSquare viewOfSquare(Position position) {
    if (!isValidPosition(position)) {
      throw new IllegalArgumentException("Invalid position argument.");
    }

    Square square = squares.get(position);
    return new ImmutableSquare(square.wordMultiplier(), square.letterMultiplier(),
            square.getLetterTile(), Collections.unmodifiableList(square.getSpecialTiles()));
  }


  /**
   * Returns a string representation of the board. Tiles are represented as follows:
   *
   * <pre>
   * +---+
   * | L |
   * |l w|
   * | s |
   * +---+
   *
   * L = letter on that space or blank
   * l = letter multiplier or blank (if 1)
   * w = word multiplier or blank (if 1)
   * s = special tile on tht space or blank
   * </pre>
   *
   * @return String representation of board. Caution: It's pretty damn big!
   */
  @Override
  public String toString() {
    int tileWidth = 3;
    String rowDividerLine = rowDivider(tileWidth) + "\n";
    String accum = rowDividerLine;

    for (int r = 0; r < height; r++) {
      String letterAccum = "|";
      String multiplierAccum = "|";
      String specialAccum = "|";
      for (int c = 0; c < width; c++) {
        // Print Letter
        Square square = squares.get(new Position(r, c));
        if (square.hasLetterTile()) {
          letterAccum += String.format("%s", square.getLetterTile().toString());
        } else {
          letterAccum += "   ";
        }
        // Multipliers
        multiplierAccum += String.format("%s %s",
                square.letterMultiplier() == 1 ? " ":square.letterMultiplier(),
                square.wordMultiplier() == 1 ? " ":square.wordMultiplier());
        // Special Tile
        if (square.hasSpecialTiles()) {
          specialAccum += " S ";
        } else {
          specialAccum += "   ";
        }
        // Add dividers
        letterAccum += "|";
        multiplierAccum += "|";
        specialAccum += "|";
      }
      accum += letterAccum + "\n";
      accum += multiplierAccum + "\n";
      accum += specialAccum + "\n";
      accum += rowDividerLine;
    }
    accum += "\n";
    return accum;
  }

  private String rowDivider(int tileWidth) {
    String accum = "+";
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < tileWidth; j++) {
        accum += "-";
      }
      accum += "+";
    }
    return accum;
  }

  private boolean hasAdjacentLetter(Position position) {
    for (Direction direction : Direction.values()) {
      Position next = nextPosition(position, direction);
      if (next != null && squares.get(next).hasLetterTile()) {
        return true;
      }
    }
    return false;
  }


  private PlayedWord getContinuousWord(Position position, Direction backwards, Direction forwards) {
    if (!squares.get(position).hasLetterTile()) {
      throw new IllegalArgumentException("No tile exists at given position.");
    }

    /* First, we move backwards from the given position to get to the start of the word.
    * We move backwards as long as we find letters before the current position, or run into the edge of the board. */
    Position startPosition = position;
    Position next = nextPosition(startPosition, backwards);
    while (next != null && squares.get(next).hasLetterTile()) {
      startPosition = next;
      next = nextPosition(startPosition, backwards);
    }

    /* PlayedWord is an immutable object, so we need to store its letters and score in local variables before
    * creating the PlayedWord object. */
    List<Letter> letterList = new ArrayList<>();
    int scoreAccumulator = 0;
    int scoreMultiplier = 1;

    /* We have the first position, so we add that letter, and all letters in a continuous sequence after it to our
    * list of letters for the word. Each time a letter is added, the "scoreAccumulator" is incremented by the amount
    * contributed by that specific letter. The "scoreMultiplier" is updated separately, because its effect should not
    * take effect until the total of the letter scores has been summed.
    */
    Square startSquare = squares.get(startPosition);
    letterList.add(startSquare.getLetterTile().letter());
    scoreAccumulator += startSquare.getLetterTile().points() * startSquare.letterMultiplier();
    scoreMultiplier *= startSquare.wordMultiplier();

    next = nextPosition(startPosition, forwards);
    while (next != null && squares.get(next).hasLetterTile()) {
      Square nextSquare = squares.get(next);
      LetterTile nextLetterTile = nextSquare.getLetterTile();
      letterList.add(nextLetterTile.letter());
      next = nextPosition(next, forwards);
      scoreAccumulator += nextLetterTile.points() * nextSquare.letterMultiplier();
      scoreMultiplier *= nextSquare.wordMultiplier();
    }

    /* In scrabble, a word must have at least two letters. */
    if (letterList.size() > 1) {
      int totalScore = scoreAccumulator * scoreMultiplier;
      return new PlayedWord(letterList, totalScore);
    }
    return null;
  }

  private Position nextPosition(Position currentPosition, Direction d) {
    Position next = d.adjacent(currentPosition);
    return isValidPosition(next) ? next : null;
  }

}
