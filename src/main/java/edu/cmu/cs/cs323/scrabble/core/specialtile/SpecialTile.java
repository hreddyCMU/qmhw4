package edu.cmu.cs.cs323.scrabble.core.specialtile;

import edu.cmu.cs.cs323.scrabble.core.board.Board;
import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGameListener;

import java.util.List;

/**
 * Represents a "Special Tile" in the SWS game. When triggered, Special Tiles
 * perform some sort of action on the current game, usually to the advantage of
 * the person placing it.
 */
public interface SpecialTile {
  /**
   * Carries out the specialized action the tile is designed to implement.
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
  void performAction(SWSGame game, Board board, Position position, List<SWSGameListener> listeners);

  /**
   * Owner of the tile.
   *
   * @return Owner of the tile.
   */
  Player owner();

  @Override
  String toString();

}
