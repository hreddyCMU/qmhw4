package edu.cmu.cs.cs323.scrabble.gui;

import edu.cmu.cs.cs323.scrabble.core.board.DefaultBoard;
import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGameListener;
import edu.cmu.cs.cs323.scrabble.core.game.Turn;
import edu.cmu.cs.cs323.scrabble.gui.board.BoardPanel;
import edu.cmu.cs.cs323.scrabble.gui.letters.TileRackPanel;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.GridLayout;
import java.util.List;

/**
 * Frame consisting of a SWS view for a single player.
 *
 *
 * <pre>
 * _____________________________________
 * |                    |              |
 * |                    |    Scores    |
 * |                    |______________|
 * |       Board        |              |
 * |                    |    Actions   |
 * |____________________|______________|
 * |                 |                 |
 * |  Letter Tiles   |  Special Tiles  |
 * |_________________|_________________|
 * </pre>
 */
public class PlayerFrame extends JFrame implements SWSGameListener {
  private static final String GAME_TITLE = "Scrabble With Stuff";
  private static final String GAME_OVER_FMT = "Game Over!!\nThe winner is: %s";
  private final SWSGame game;
  private final Player player;
  private final BoardPanel boardPanel;
  private final TileRackPanel tileRackPanel;
  private final ActionsPanel actionsPanel;
  private final ScorePanel scorePanel;

  /**
   * Constructor. Creates a JFrame holding a player's view of the board and any relevant turn
   * information.
   *
   * @param game Game associated with the PlayerFrame.
   * @param player Player associated with the PlayerFrame.
   * @param competitors List of all players in the game (necessary to keep track of score).
   */
  public PlayerFrame(SWSGame game, Player player, List<Player> competitors) {
    super(GAME_TITLE + ": " + player.name());
    this.game = game;
    this.player = player;

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setBackground(ColorScheme.BOARD_BG);

    boardPanel = new BoardPanel(DefaultBoard.HEIGHT, DefaultBoard.WIDTH, game, player);
    actionsPanel = new ActionsPanel(this.game, player);
    tileRackPanel = new TileRackPanel(player);
    scorePanel = new ScorePanel(game, competitors);

    /* Add score  and actions to the top right panel. */
    Box topRight = Box.createVerticalBox();
    JPanel alignmentFix = new JPanel(new GridLayout(1,1));
    alignmentFix.add(scorePanel);
    topRight.add(alignmentFix);
    topRight.add(actionsPanel);
    topRight.setBackground(getBackground());
    /* Add board and top right panel to top panel. */
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.add(boardPanel);
    topPanel.add(topRight);
    topPanel.setBackground(getBackground());

    /* Actions appear on top, the board in the middle, and the rack on the bottom. */
    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(topPanel);
    add(tileRackPanel);

    /* Add this as a game listener only after the UI is ready to be laid out. */
    this.game.addSWSGameListener(this);

    pack();
  }

  @Override
  public void squareChanged(Position position) {
    SwingUtilities.invokeLater(() -> {
      boardPanel.refresh(position);
      pack();
    });
  }

  @Override
  public void playerChanged(Player player) {
    SwingUtilities.invokeLater(() -> {
      tileRackPanel.refresh();
      pack();
    });
  }

  @Override
  public void turnChanged(Turn turn) {
    SwingUtilities.invokeLater(() -> {
      actionsPanel.update(turn);
      if (turn.player().equals(player)) {
        toFront();
        repaint();
      }
    });
  }

  @Override
  public void gameEnded(Player winner) {
    String message = String.format(GAME_OVER_FMT, winner.toString());
    SwingUtilities.invokeLater(() -> {
      JOptionPane.showMessageDialog(null, message);
    });
  }

  @Override
  public void errorOccurred(Player target, String message) {
    if (player.equals(target)) {
      SwingUtilities.invokeLater(() -> {
        JOptionPane.showMessageDialog(this, message);
      });
    }
  }
}
