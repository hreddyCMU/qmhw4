package edu.cmu.cs.cs323.scrabble.gui.letters;

import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;
import edu.cmu.cs.cs323.scrabble.gui.special.SpecialTilePanel;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.List;

/**
 * Provides a graphical representation of the tiles on the players tile rack.
 */
public class TileRackPanel extends JPanel {
  private static final int T_PAD = 0;
  private static final int L_PAD = 0;
  private static final int B_PAD = 10;
  private static final int R_PAD = 0;
  private final Player player;

  /**
   * Constructor. Creates a representation of the given player's tile rack.
   *
   * Requires: {@code player} is non-null.
   *
   * @param player Player whom the tile rack is meant for.
   */
  public TileRackPanel(Player player) {
    this.player = player;
    setBorder(BorderFactory.createEmptyBorder(T_PAD, L_PAD, B_PAD, R_PAD));
    setBackground(ColorScheme.TILE_RACK_BG);
    refresh();
  }

  /** Updates the view to portray current information */
  public void refresh() {
    removeAll();
    add(Box.createGlue());


    /* Sort the tiles so duplicates will appear next to each other. */
    List<LetterTile> sortedTiles = player.letterTiles();
    sortedTiles.sort((LetterTile l1, LetterTile l2) -> l1.toString().compareTo(l2.toString()));
    for (LetterTile letterTile : sortedTiles) {
      add(new LetterTilePanel(letterTile));
    }

    List<SpecialTile> specialTiles = player.specialTiles();
    if (specialTiles.size() > 0) {
      add(Box.createVerticalStrut(getHeight() * 3 / 4));
      for (SpecialTile specialTile : specialTiles) {
        add(new SpecialTilePanel(specialTile));
      }
    }

    add(Box.createGlue());
    setPreferredSize(new Dimension(getWidth(), LetterTilePanel.H + B_PAD));
    repaint();
  }
}
