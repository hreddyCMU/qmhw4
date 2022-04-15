package edu.cmu.cs.cs323.scrabble.gui;

import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGameListener;
import edu.cmu.cs.cs323.scrabble.core.game.Turn;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays and updates score information for the given players.
 */
public class ScorePanel extends JPanel implements SWSGameListener {
  private static final int LABEL_HEIGHT = 60;
  private static final String NAME_SCORE_FMT = "%s: %d";
  private static final String TITLE = "Scores";
  private final Map<Player,JLabel> scores = new HashMap<>();

  /**
   * Constructor. Creates a panel that updates score information for the given players.
   *
   * @param game Game being scored.
   * @param players List of players in game.
   */
  public ScorePanel(SWSGame game, List<Player> players) {
    setBackground(ColorScheme.SCORE_BG);
    setForeground(ColorScheme.SCORE_FG);
    TitledBorder titledBorder = BorderFactory.createTitledBorder(TITLE);
    titledBorder.setTitleColor(ColorScheme.SCORE_FG);
    setBorder(titledBorder);

    /* Display scores in one left-aligned column. */
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    for (Player player : players) {
      JLabel playerLabel = labelForPlayer(player);
      scores.put(player, playerLabel);
      add(playerLabel);
    }
    setMaximumSize(new Dimension(Short.MAX_VALUE, LABEL_HEIGHT * players.size() + 1));
    game.addSWSGameListener(this);
  }

  @Override
  public void squareChanged(Position position) {
    // Do nothing.
  }

  @Override
  public void playerChanged(Player player) {
    if (scores.containsKey(player)) {
      SwingUtilities.invokeLater(() -> {
        scores.get(player).setText(scoreString(player));
      });
    }
  }

  @Override
  public void turnChanged(Turn turn) {
    // Do nothing.
  }

  @Override
  public void gameEnded(Player winner) {
    // Do nothing.
  }

  @Override
  public void errorOccurred(Player target, String message) {
    // Do nothing.
  }

  private String scoreString(Player player) {
    return String.format(NAME_SCORE_FMT, player.name(), player.getScore());
  }

  private JLabel labelForPlayer(Player player) {
    JLabel label = new JLabel(scoreString(player));
    label.setBackground(ColorScheme.SCORE_BG);
    label.setForeground(ColorScheme.SCORE_FG);
    label.setPreferredSize(new Dimension(getWidth(), LABEL_HEIGHT));
    return label;
  }
}
