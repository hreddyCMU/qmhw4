package edu.cmu.cs.cs323.scrabble.gui.util;

import edu.cmu.cs.cs323.scrabble.core.board.square.Square;

import java.awt.Color;

/**
 * GUI Colors for SWS
 */
public class ColorScheme {

  /* Board */
  public static final Color BOARD_BG = new Color(128, 32, 16);
  public static final Color BOARD_BETWEEN = new Color(240, 220, 200);

  /* Squares */
  private static final Color SQ_LETTER_2 = new Color(4, 120, 236);
  private static final Color SQ_LETTER_3 = new Color(32, 64, 196);
  private static final Color SQ_WORD_2 = new Color(236, 96, 86);
  private static final Color SQ_WORD_3 = new Color(180, 40, 20);
  private static final Color SQ_DEFAULT = new Color(220, 150,110);
  public static final Color SQ_FG = Color.WHITE;

  /* Score area */
  public static final Color SCORE_BG = BOARD_BG;
  public static final Color SCORE_FG = BOARD_BETWEEN;

  /**
   * @param square Square whose color we are inquiring about. Wow that sounds super formal.
   * @return Background color of the square based on its multipliers.
   */
  public static Color colorForSquare(Square square) {
    if (square.letterMultiplier() == 2) {
      return SQ_LETTER_2;
    } else if (square.letterMultiplier() == 3) {
      return SQ_LETTER_3;
    } else if (square.wordMultiplier() == 2) {
      return SQ_WORD_2;
    } else if (square.wordMultiplier() == 3) {
      return SQ_WORD_3;
    } else {
      return SQ_DEFAULT;
    }
  }

  /* Tiles */
  public static final Color TILE_BG = new Color(240, 190, 130);
  public static final Color TILE_FG = new Color(50, 30, 20);
  public static final Color TILE_RACK_BG = BOARD_BG;

  public static final Color SPECIAL_TILE_BG = new Color(156, 185, 226, 196);
  public static final Color SPECIAL_TILE_FG = new Color(225, 225, 225);

}
