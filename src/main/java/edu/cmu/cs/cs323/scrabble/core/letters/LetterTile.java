package edu.cmu.cs.cs323.scrabble.core.letters;

/**
 * Represents a standard Scrabble game tile. Each letter tile has a letter on
 * its face, and an associated point value (dependent on the letter).
 */
public final class LetterTile {
  private final Letter letter;
  private final int points;

  /**
   * Default constructor. Creates a tile with the given letter on it.
   * 
   * @param letter
   *          Letter on the face of the tile.
   */
  public LetterTile(Letter letter) {
    this.letter = letter;
    this.points = pointsForLetter(this.letter);
  }

  /**
   * Returns the value of a tile associated with a specific letter.
   * 
   * @param letter
   *          The letter on the tile we want to know the points for.
   * @return The number of points for a tile of the given letter.
   */
  private static int pointsForLetter(Letter letter) {
    // CHECKSTYLE:OFF
    switch (letter) {
    case D:
    case G:
      return 2;
    case B:
    case C:
    case M:
    case P:
      return 3;
    case F:
    case H:
    case V:
    case W:
    case Y:
      return 4;
    case K:
      return 5;
    case J:
    case X:
      return 8;
    case Q:
    case Z:
      return 10;
    default:
      return 1;
    }
    // CHECKSTYLE:ON
  }

  @Override
  public String toString() {
    return String.format("%s:%d", letter.toString(), points);
  }
  
  /**
   * @return the point value of a this tile, based upon the letter.
   */
  public int points() {
    return points;
  }

  /**
   * @return the letter on the face of this tile.
   */
  public Letter letter() {
    return letter;
  }

}
