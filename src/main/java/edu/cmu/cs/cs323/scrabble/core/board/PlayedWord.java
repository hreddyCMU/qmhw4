package edu.cmu.cs.cs323.scrabble.core.board;

import edu.cmu.cs.cs323.scrabble.core.letters.Letter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a word (formed by a horizontal or vertical sequence of letters) on a scrabble board.
 * This is a representation of the played word itself, just the sequence of letters and associated
 * value.
 *
 * PlayedWord is an immutable class.
 */
public final class PlayedWord {
    private final List<Letter> letters;
    private final int score;

  /**
   * Constructor. Instantiates an immutable object representing a word played in SWS, and its
   * associated score.
   *
   * @param letters
   * Letters, in order, that comprise the word.
   * @param score
   * Point value of the word when played.
   */
    public PlayedWord(List<Letter> letters, int score) {
        this.letters = new ArrayList<>(letters);
        this.score = score;
    }

    /**
     * Returns the number of points the word is worth.
     *
     * @return Score, in points, gained from placing this word.
     */
    public int score() {
        return score;
    }

    /**
     * Returns a string representing the sequence of letters forming the word.
     *
     * @return String representation of the letters forming the word.
     */
    @Override
    public String toString() {
        String str = "";
        for (Letter letter : letters) {
            str += letter.toString();
        }
        return str;
    }

}
