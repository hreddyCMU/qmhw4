package edu.cmu.cs.cs323.scrabble.core.board.square;

import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.List;

/**
 * Represents a single square on a Scrabble Game Board. A square can be empty,
 * occupied by exactly one LetterTile, or occupied by one or more special tiles
 * (but no letter tiles).
 *
 * Squares also have <em>multipliers</em> for both words and letters.
 * Multipliers allow squares to influence the score of the words/letters placed
 * on top of them. The 'letter' mutliplier is applied only to the score of the
 * letter on the square. The word multiplier is applied after the score of the
 * entire word is calculated.
 */
public interface Square {
  /**
   * @return The coefficient to multiply the score of the entire word by.
   */
  int wordMultiplier();

  /**
   * @return The coefficient to multiply the letter score by.
   */
  int letterMultiplier();

  /**
   * @return A list of special tiles on the square.
   */
  List<SpecialTile> getSpecialTiles();

  /**
   * Removes all special tiles from this square.
   */
  void clearSpecialTiles();

  /**
   * @return true if there is at least one special tile on this square, false
   *         otherwise.
   */
  boolean hasSpecialTiles();

  /**
   * 
   * @return true if a letter tile has been placed on the square, false if it's
   *         open.
   */
  boolean hasLetterTile();

  /**
   * Puts the given special tile on this square.
   * 
   * Requires: !hasLetterTile()
   * 
   * @param tileToPlace
   *          new special tile to be placed onto square.
   */
  void placeSpecialTile(SpecialTile tileToPlace);

  /**
   * Puts the given letter tile on this square.
   * 
   * Requires: !hasLetterTile()
   * 
   * @param tileToPlace
   *          new tile to be placed onto square.
   */
  void placeLetterTile(LetterTile tileToPlace);

  /**
   * @return the letter tile on the square, null otherwise.
   */
  LetterTile getLetterTile();

  /**
   * Removes and returns the letter tile from the square. If there is no letter
   * tile on the board, null is returned.
   * 
   * 
   * @return The letter tile currently on the square, or null if it doesn't
   *         exist.
   */
  LetterTile removeLetterTile();

}
