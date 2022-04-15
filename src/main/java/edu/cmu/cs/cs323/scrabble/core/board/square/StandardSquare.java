package edu.cmu.cs.cs323.scrabble.core.board.square;

/**
 * Represents the most common type of square on a scrabble board, that is one whose multipliers
 * have no effect on the score of a word.
 */
public class StandardSquare extends SquareImpl {
  private static final int W_MULTIPLIER = 1;
  private static final int L_MULTIPLIER = 1;

  @Override
  public int letterMultiplier() {
    return L_MULTIPLIER;
  }

  @Override
  public int wordMultiplier() {
    return W_MULTIPLIER;
  }
}
