package edu.cmu.cs.cs323.scrabble.core.board.square;

/**
 * Represents a square that doubles the score of the current letter, but does
 * not modify the score of the entire word.
 */
public class DoubleLetterScoreSquare extends SquareImpl {
  private static final int W_MULTIPLIER = 1;
  private static final int L_MULTIPLIER = 2;

  @Override
  public int letterMultiplier() {
    return L_MULTIPLIER;
  }

  @Override
  public int wordMultiplier() {
    return W_MULTIPLIER;
  }
}
