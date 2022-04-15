package edu.cmu.cs.cs323.scrabble.core.game;

import edu.cmu.cs.cs323.scrabble.core.board.Board;
import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;
import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a placement of tiles on a SWS board by a player.
 *
 * The move is supposed to ONLY worry about a specific tile placement. It is immutable, and doesn't
 * and because it will be created by a client, it may not be a valid move, so I chose not to
 * have it associated with a board upon creation, so score/word calculations require the board
 * to be passed in. This isn't ideal, but it works.
 */
public final class Move {
  private final Map<Position, LetterTile> placements;
  private final Position upperLeft;
  private final Position lowerRight;

  /**
   * Represents the orientation of the move on the board, either left to right or right to left.
   */
  public enum Orientation {
    ACROSS, DOWN
  }

  /**
   * Constructor. Creates a move representing a placement of tiles with their corresponding
   * positions.
   *
   * Requires there is at least one placement.
   *
   * @param placements A map of positions, and the tiles that should be placed in them.
   */
  public Move(Map<Position, LetterTile> placements) {
    if (placements.size() < 1) {
      throw new IllegalArgumentException("Move must consist of at least one tile placement.");
    }
    this.placements = new HashMap<>(placements);
    this.upperLeft = computeUpperLeft();
    this.lowerRight = computeLowerRight();
  }

  /**
   * Gets the position of the tile that was placed closest to the origin for this Move. This is the
   * tile that is the furthest up or furthest to the left.
   *
   * @return The position of the tile placed as part of this move that is closest to the origin.
   */
  public Position upperLeft() {
    return upperLeft;
  }

  /**
   * Gets the position of the tile that was placed furthest from the origin for this Move. This is
   * the tile that is the
   * furthest down or furthest to the right.
   *
   * @return The position of the tile placed as part of this move that is furthest from the origin.
   */
  public Position lowerRight() {
    return lowerRight;
  }

  /**
   * Gets the tile placements associated with this move.
   *
   * @return Map of placements positions and their associated tiles.
   */
  public Map<Position,LetterTile> placements() {
    return new HashMap<>(placements);
  }

  /**
   * Retrieve all words that were created by the placement of this Move.
   *
   * Requires board contains all of the positions included in this move's placements. Equivalently,
   * for every position in this Move's placements, board.isValidPosition(position) should be true.
   *
   * @param board The board instance the move is played on.
   * @return A list of words created by this move.
   */
  public List<PlayedWord> words(Board board) {
    List<Position> positionList = positionList();
    for (Position position : positionList) {
      if (!board.isValidPosition(position)) {
        throw new IllegalArgumentException("Letter placements incompatable with board argument.");
      }
    }

    List<PlayedWord> words = new ArrayList<>();
    if (positionList.size() > 1) {
      switch (orientation()) {
        case ACROSS:
          words.add(board.horizontalWordIncluding(positionList.get(0)));
          for (Position position : positionList) {
            PlayedWord tempWord = board.verticalWordIncluding(position);
            if (tempWord != null) {
              words.add(tempWord);
            }
          }
          break;
        case DOWN:
          words.add(board.verticalWordIncluding(positionList.get(0)));
          for (Position position : positionList) {
            PlayedWord tempWord = board.horizontalWordIncluding(position);
            if (tempWord != null) {
              words.add(tempWord);
            }
          }
          break;
        default:
        /* No default is needed, because we have a case for each enum, but checkstyle complains. */
          break;
      }
    } else {
      assert(positionList.size() == 1);
      /* If only one tile is being placed, there is really no orientation, so just try to get either
       * of the possible words. */
      PlayedWord horizontal = board.horizontalWordIncluding(positionList.get(0));
      if (horizontal != null) {
        words.add(horizontal);
      }
      PlayedWord vertical = board.verticalWordIncluding(positionList.get(0));
      if (vertical != null) {
        words.add(vertical);
      }
    }
    return words;
  }

  /**
   * Computes the total score for all of the words created by this move.
   *
   * @param board Game board the score should be calculated in context of.
   * @return sum of the scores of the words created by this move.
   */
  public int totalScore(Board board) {
    int sum = 0;
    for (PlayedWord word : words(board)) {
      sum = word.score();
    }
    return sum;
  }

  // TODO Consider making this public, and not giving public access to `placements`
  private List<Position>  positionList() {
    return new ArrayList<>(placements.keySet());
  }

  /**
   * returns the orientation (ACROSS,DOWN) of the tiles placed.
   *
   * @return orientation letter tiles are to be placed in.
   */
  public Orientation orientation() {
    return (upperLeft.row == lowerRight.row) ? Orientation.ACROSS:Orientation.DOWN;
  }

  private Position computeUpperLeft() {
    List<Position> positionList = positionList();
    Position mostLeft = positionList.get(0);
    for (Position position : positionList) {
      if (mostLeft.compareTo(position) > 0) {
        mostLeft = position;
      }
    }
    return mostLeft;
  }

  private Position computeLowerRight() {
    List<Position> positionList = positionList();
    Position mostRight = positionList.get(0);
    for (Position position : positionList) {
      if (mostRight.compareTo(position) < 0) {
        mostRight = position;
      }
    }
    return mostRight;
  }

  
}
