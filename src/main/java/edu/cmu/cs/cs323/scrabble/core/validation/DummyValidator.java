package edu.cmu.cs.cs323.scrabble.core.validation;

import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;

/**
 * A very, very accepting validator.
 */
public class DummyValidator implements WordValidator {
  @Override
  public boolean isValidWord(PlayedWord w) {
    return w.toString().length() > 0;
  }
}
