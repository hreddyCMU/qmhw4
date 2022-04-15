package edu.cmu.cs.cs323.scrabble.core.board.square;

/**
 * Represents a square that triples the score of the entire word, but does not
 * modify the score of the letter placed on it.
 */
public class TripleWordScoreSquare extends SquareImpl {
  private static final int W_MULTIPLIER = 3;
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
