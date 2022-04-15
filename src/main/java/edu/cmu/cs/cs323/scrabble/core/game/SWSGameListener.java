package edu.cmu.cs.cs323.scrabble.core.game;

import edu.cmu.cs.cs323.scrabble.core.board.Position;

/**
 * Event Listener for Scrabble With Stuff
 */
public interface SWSGameListener {

  /**
   * Called when the square at the given position on the board has been modified.
   *
   * @param position position of the modified square on the board.
   */
  void squareChanged(Position position);

  /**
   * Called when a player's state has changed.
   *
   * @param player player whose state changed.
   */
  void playerChanged(Player player);

  /**
   * Called when the game's current player has changed, therefore beginning a new turn.
   *
   * @param turn new turn.
   */
  void turnChanged(Turn turn);

  /**
   * Called when the game has been won.
   *
   * @param winner Player who won the game.
   */
  void gameEnded(Player winner);

  /**
   * Called when there has been an error in the game. This does not have to be a fatal error, it
   * could be something as simple as a specific move was invalidated.
   *
   * @param target Player who should receive the message.
   * @param message Message describing error.
   */
  void errorOccurred(Player target, String message);

}
