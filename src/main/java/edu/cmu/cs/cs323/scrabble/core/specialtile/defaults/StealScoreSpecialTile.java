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
 * Special Tile that causes the player who placed the tile to earn the points
 * the player who activated it would have gained.
 */
public class StealScoreSpecialTile implements SpecialTile {

  private Player owner;

  StealScoreSpecialTile(Player owner) {
    this.owner = owner;
  }

  /**
   * Unique identifier describing the tile.
   *
   * @return identifier.
   */
  public static String identifier() {
    return "Steal Score";
  }

  /**
   * Steals (subtracts) the points for the last move from the player who played it, and gives (adds)
   * them to the owner of the special style.
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
      /* Since the player's score is updated by game, we subtract their score, and add to the
       * owner's score. */
      player.setScore(player.getScore() - word.score());
      owner.setScore(owner.getScore() + word.score());
    }
    listeners.forEach(listener -> {
      listener.playerChanged(player);
      listener.playerChanged(owner);
    });
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
