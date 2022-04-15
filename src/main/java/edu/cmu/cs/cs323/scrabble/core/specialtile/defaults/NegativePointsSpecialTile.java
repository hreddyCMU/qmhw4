package edu.cmu.cs.cs323.scrabble.core.specialtile.defaults;

import edu.cmu.cs.cs323.scrabble.core.board.Board;
import edu.cmu.cs.cs323.scrabble.core.board.PlayedWord;
import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGameListener;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.List;

/**
 * Special Tile that subtracts the points for a given move rather than adding
 * them.
 */
class NegativePointsSpecialTile implements SpecialTile {

  private Player owner;

  NegativePointsSpecialTile(Player owner) {
    this.owner = owner;
  }

  public static String identifier() {
    return "Negate Points";
  }

  /**
   * Gives the player negative points instead of positive points for the last word they played.
   *
   * @param game
   *          Instance of the current SWS game.
   * @param board
   *          Game board.
   * @param position
   *          Position the special tile was in when triggered.
   * @param listeners
   *          SWSGameListeners associated with the current game.
   */
  @Override
  public void performAction(SWSGame game, Board board, Position position,
                            List<SWSGameListener> listeners) {
    assert(game.lastMove() != null);

    Player player = game.currentPlayer();
    for (PlayedWord word : game.lastMove().words(board)) {
      /* Since the player's score is updated by game, we need to subtract double the score. */
      player.setScore(player.getScore() - 2 * word.score());
    }
    listeners.forEach(listener -> listener.playerChanged(player));
  }

  @Override
  public Player owner() {
    return owner;
  }

  @Override
  public String toString() {
    return identifier();
  }

}
