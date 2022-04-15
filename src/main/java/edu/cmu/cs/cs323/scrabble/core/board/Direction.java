package edu.cmu.cs.cs323.scrabble.core.board;

/**
 * Represents directional relationships on the game board, relating to
 * {@code Position}s. The fields of the enum store the row and column
 * differentials to make a move in that direction, allowing for generic
 * computaiton of the next available position in that direction.
 *
 * See also: {@code nextPosition}
 */
public enum Direction {
  UP(-1, 0), DOWN(1, 0), RIGHT(0, 1), LEFT(0, -1);

  private final int rowDelta;
  private final int colDelta;

  Direction(int rowDelta, int colDelta) {
    this.rowDelta = rowDelta;
    this.colDelta = colDelta;
  }

  /**
   * Returns the position next to this one, in 'this' direction.
   *
   * @param position
   * Starting position.
   * @return Position adjacent to this one, in the given direction. If no such position exists (positions are only
   * defined for positive values), returns {@code null}.
   */
  Position adjacent(Position position) {
    int newRow = position.row + rowDelta;
    int newCol = position.col + colDelta;
    if (newRow >= 0 || newCol >= 0) {
      return new Position(newRow, newCol);
    }
    return null;
  }
}
