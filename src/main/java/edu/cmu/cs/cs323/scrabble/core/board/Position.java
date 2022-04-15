package edu.cmu.cs.cs323.scrabble.core.board;

import java.util.List;

/**
 * Represents an ordered pair of a row and column.
 */
public final class Position implements Comparable<Position> {
  public final int row;
  public final int col;

  /**
   * Constructor. Represents a point at the given row and column indicies.
   *
   * @param row Row index of position.
   * @param col Column Index of position.
   */
  public Position(int row, int col) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Row and column must be positive values.");
    }
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean equals(Object other) {
    Position otherPosition = (Position) other;
    return this.row == otherPosition.row && this.col == otherPosition.col;
  }

  @Override
  public int hashCode() {
    return 31 * row + col;
  }

  /**
   * Computes distance between this and another point.
   *
   * @param other Point to compute the distance to.
   * @return Euclidian distance between this point and the argument.
   */
  public double distance(Position other) {
    // Euclidian distance formula
    return Math.sqrt(Math.pow(row - other.row, 2) + Math.pow(col - other.col, 2));
  }

  /**
   * Computes the point's distance from the origin.
   *
   * @return Euclidian distance from the origin.
     */
  public double distance() {
    return distance(new Position(0,0));
  };


  /**
   * Compares two positions based on their distance from the origin.
   *
   * @param other other position to compare this one to.
   * @return 0 if positions are equal, < 0 if {@code other} > {@code this}, > 0 if {@code this} > {@code other}
   */
  @Override
  public int compareTo(Position other) {
    if (equals(other)) {
      return 0;
    }
    return distance() < other.distance() ? -1:1;
  }


  /**
   * Determines whether all of the positions in a list have the same row (i.e. they are horizontal).
   *
   * @param positions list of positions, whose rows may or may not be equal.
   * @return true if all items in list are horizontal, false otherwise. If the list is empty, or a singleton, it is
   * considered to be horizontal.
   */
  public static boolean rowsAllEqual(List<Position> positions) {
    // Singletons and Empty lists are both considered horizontal
    if (positions.size() < 2) {
      return true;
    }
    // Assume the first position's row is your base, and return false if any position's row does not match the base row
    int row = positions.get(0).row;
    for (Position position : positions) {
      if (position.row != row) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines whether all of the positions in a list have the same column (i.e. they are vertical).
   *
   * @param positions list of positions, whose columns may or may not be equal.
   * @return true if all items in list are vertical, false otherwise. If the list is empty, or a singleton, it is
   * considered to be vertical.
   */
  public static boolean colsAllEqual(List<Position> positions) {
    // Singletons and Empty lists are both considered vertical
    if (positions.size() < 2) {
      return true;
    }
    // Assume the first position's col is your base, and return false if any position's col does not match the base col
    int col = positions.get(0).col;
    for (Position position : positions) {
      if (position.col != col) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return String.format("(%d,%d)", row, col);
  }
}


