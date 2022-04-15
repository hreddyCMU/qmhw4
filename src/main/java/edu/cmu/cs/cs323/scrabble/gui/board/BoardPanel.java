package edu.cmu.cs.cs323.scrabble.gui.board;

import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.gui.board.square.SquarePanel;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * GUI representation of the Scrabble With Stuff Board.
 */
public class BoardPanel extends JPanel {
  private Map<Position,SquarePanel> squares = new HashMap<>();
  private Player player;
  private SWSGame game;

  /**
   * Constructor. Creates a JPanel representing the given player's view of a SWS board with the
   * given dimensions.
   *
   * @param nrows Number of rows on the board.
   * @param ncols Number of columns on the board.
   * @param game Game this board belongs to.
   * @param player Player viewing the board.
   */
  public BoardPanel(int nrows, int ncols, SWSGame game, Player player) {
    if (nrows < 1 || ncols < 1) {
      throw new IllegalArgumentException("Must have size > 1");
    }
    this.player = player;
    this.game = game;
    setBackground(ColorScheme.BOARD_BG);

    JPanel board = new JPanel();
    board.setBackground(ColorScheme.BOARD_BETWEEN);
    board.setLayout(new GridLayout(nrows, ncols));
    for (int r = 0; r < nrows; r++) {
      for (int c = 0; c < ncols; c++) {
        Position position = new Position(r,c);
        SquarePanel squarePanel = new SquarePanel();
        squares.put(position, squarePanel);
        board.add(squarePanel);
        refresh(position);
      }
    }
    board.setVisible(true);
    add(board);

    setVisible(true);
  }

  /**
   * Updates graphics for tile at {@code position}.
   *
   * @param position Position to update.
   */
  public void refresh(Position position) {
    if (squares.containsKey(position)) {
      squares.get(position).update(game.viewOfSquare(player, position));
    }
  }
}
