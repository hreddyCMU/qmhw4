package edu.cmu.cs.cs323.scrabble.gui.letters;

import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * GUI Representation of a Letter Tile.
 */
public class LetterTilePanel extends JPanel {
  public static final int W = 40;
  public static final int H = W;
  private static final int POINT_PAD_T = 0;
  private static final int POINT_PAD_B = 1;
  private static final int POINT_PAD_L = 0;
  private static final int POINT_PAD_R = 1;

  /**
   * Constructor. Creates a graphical representation of a letter tile.
   *
   * @param letterTile Letter tile to represent.
   */
  public LetterTilePanel(LetterTile letterTile) {
    setBackground(ColorScheme.TILE_BG);
    setBorder(BorderFactory.createRaisedBevelBorder());

    /* Create a large size letter in the center of the tile */
    JLabel letterLabel = new JLabel(String.format("<html><font size=+1>%s</font><html>",
                                      letterTile.letter().toString()));
    letterLabel.setHorizontalAlignment(JLabel.CENTER);
    letterLabel.setHorizontalTextPosition(JLabel.CENTER);
    letterLabel.setVerticalAlignment(JLabel.CENTER);
    letterLabel.setForeground(ColorScheme.TILE_FG);

    /* Put a small score in the bottom left corner */
    JLabel pointsLabel = new JLabel(String.format("<html><font size=-4>%d</font><html>",
            letterTile.points()));
    pointsLabel.setHorizontalAlignment(JLabel.RIGHT);
    pointsLabel.setVerticalAlignment(JLabel.CENTER);
    pointsLabel.setForeground(ColorScheme.TILE_FG);
    pointsLabel.setBorder(BorderFactory.createEmptyBorder(POINT_PAD_T, POINT_PAD_L,
                                                            POINT_PAD_B, POINT_PAD_R));

    setLayout(new BorderLayout());
    add(letterLabel, BorderLayout.CENTER);
    add(pointsLabel, BorderLayout.PAGE_END);

    setPreferredSize(new Dimension(W,H));
  }
}
