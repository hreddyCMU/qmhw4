package edu.cmu.cs.cs323.scrabble.gui.special;

import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * GUI Representation of a Letter Tile.
 */
public class SpecialTilePanel extends JPanel {
  public static final int W = 40;
  public static final int H = W;

  /**
   * Constructor. Creates a graphical representation of a special tile.
   *
   * @param specialTile Special tile to represent.
   */
  public SpecialTilePanel(SpecialTile specialTile) {
    setBackground(ColorScheme.SPECIAL_TILE_BG);
    setBorder(BorderFactory.createLoweredBevelBorder());

    /* Create a large size letter in the center of the tile */
    JLabel description = new JLabel(String.format("<html><font size=-2>%s</font></html>", specialTile));
    description.setHorizontalAlignment(JLabel.CENTER);
    description.setHorizontalTextPosition(JLabel.CENTER);
    description.setVerticalAlignment(JLabel.CENTER);
    description.setVerticalTextPosition(JLabel.CENTER);
    description.setForeground(ColorScheme.SPECIAL_TILE_FG);

    setLayout(new BorderLayout());
    add(description, BorderLayout.CENTER);

    setPreferredSize(new Dimension(W,H));

  }
}
