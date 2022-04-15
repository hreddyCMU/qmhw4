package edu.cmu.cs.cs323.scrabble.core.validation;

import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;

/**
 * Defines what words are and are not valid for a game of Scrabble.
 */
public interface WordValidator {

    /**
     * Determines if a word is an allowable scrabble play.
     *
     * @param w Word to be tested.
     * @return true if the word is valid, false if invalid.
     */
    boolean isValidWord(PlayedWord w);

}
