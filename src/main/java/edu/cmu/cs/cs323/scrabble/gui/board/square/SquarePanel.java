package edu.cmu.cs.cs323.scrabble.gui.board.square;

import edu.cmu.cs.cs323.scrabble.core.board.square.Square;
import edu.cmu.cs.cs323.scrabble.gui.letters.LetterTilePanel;
import edu.cmu.cs.cs323.scrabble.gui.special.SpecialTilePanel;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Graphical representation of a square on a SWS board.
 */
public class SquarePanel extends JPanel {
  private static final int W = LetterTilePanel.W + 1;
  private static final int H = LetterTilePanel.H + 1;
  private static final String TXT_LETTER_2 = "Double Letter Score";
  private static final String TXT_LETTER_3 = "Triple Letter Score";
  private static final String TXT_WORD_2 = "Double Word Score";
  private static final String TXT_WORD_3 = "Triple Word Score";
  private static final String TXT_DEFAULT = "";

  /**
   * Creates a blank square panel with nothing on it.
   */
  public SquarePanel() {
    setPreferredSize(new Dimension(W, H));
    setBorder(BorderFactory.createLoweredSoftBevelBorder());
  }

  /**
   * Updates panel with the information from the new square.
   *
   * @param square Square whose information should be displayed.
   */
  public void update(Square square) {
    setBackground(ColorScheme.colorForSquare(square));

    removeAll();
    setLayout(new BorderLayout());

    if (square.hasLetterTile()) {
      add(new LetterTilePanel(square.getLetterTile()), BorderLayout.CENTER);
    } else if (square.hasSpecialTiles()) {
      add(new SpecialTilePanel(square.getSpecialTiles().get(0)), BorderLayout.CENTER);
    } else {
      add(formattedLabel(textForSquare(square)));
    }
  }

  private static JLabel formattedLabel(String message) {
    String smallMessage = String.format("<html><center><font size=-4>%s</font></center></html>",
            message);
    JLabel label = new JLabel(smallMessage);
    label.setForeground(ColorScheme.SQ_FG);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setHorizontalTextPosition(JLabel.CENTER);
    label.setVerticalTextPosition(JLabel.CENTER);
    return label;
  }

  private static String textForSquare(Square square) {
    if (square.letterMultiplier() == 2) {
      return TXT_LETTER_2;
    } else if (square.letterMultiplier() == 3) {
      return TXT_LETTER_3;
    } else if (square.wordMultiplier() == 2) {
      return TXT_WORD_2;
    } else if (square.wordMultiplier() == 3) {
      return TXT_WORD_3;
    } else {
      return TXT_DEFAULT;
    }
  }

}
