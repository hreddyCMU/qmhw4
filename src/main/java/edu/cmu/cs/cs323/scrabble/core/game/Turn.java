package edu.cmu.cs.cs323.scrabble.core.game;

import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks the activity of a player across a single turn, and holds information relevant only while
 * the turn is ongoing.
 *
 * A turn consists of some combination of the following actions:
 *
 * --- Letter Tile Actions
 * 1. Swap Letter Tiles
 * 2. Place Letter Tiles
 * --- Special Tile Actions
 * 3. Buy Special Tiles
 * 4. Place Special Tile
 * ---
 * 5. Draw tiles
 *
 * Certain subsets of these actions cause conflicts. Actions 1 and 2, as well as actions 3 and 4,
 * cannot be performed on the same turn. As a rule of thumb, only one type of action can take place
 * for a given type of tile. So a player can place or swap a Letter tile, but not both on any turn.
 * Similarly, regardless of letter tile actions on the current turn, the player can purchase or
 * place a special tile, but not both.
 *
 * Also note that it is acceptable to ONLY draw tiles on a turn. This is equivalent to a "pass" in
 * regular scrabble.
 *
 */
public class Turn {
  private final Map<Position,List<SpecialTile>> triggeredSpecialTiles = new HashMap<>();
  private final Player player;
  private boolean letterActionTaken = false;
  private boolean specialActionTaken = false;
  private boolean challengeActionTaken = false;
  private boolean endActionTaken = false;
  private Move playedMove = null;

  // TODO Support concurrency

  /**
   * Constructor. Creates a new turn for the given player. Since the turn has just begun, the new
   * Turn object is set up as if no actions have been taken yet.
   *
   * Requires player is nonnull.
   *
   * @param player Player who is "active" for the current turn.
   */
  public Turn(Player player) {
    this.player = player;
  }

  /**
   * Adds the given special tile to the list of special tiles triggered on this turn.
   *
   * @param position Position the special tile was in.
   * @param triggeredSpecialTile Special tile triggered by a placement on this
   */
  public void addTriggeredSpecialTile(Position position, SpecialTile triggeredSpecialTile) {
    if (!triggeredSpecialTiles.containsKey(position)) {
      triggeredSpecialTiles.put(position, new ArrayList<>());
    }
    triggeredSpecialTiles.get(position).add(triggeredSpecialTile);
  }

  /**
   * Removes all triggered special tiles from this turn.
   */
  public void clearTriggeredSpecialTiles() {
    triggeredSpecialTiles.clear();
  }

  /**
   * Gets a (possibly empty) list of the special tiles triggered on this turn.
   *
   * @return List of triggered special tiles.
   */
  public Map<Position, List<SpecialTile>> triggeredSpecialTiles() {
    return new HashMap<>(triggeredSpecialTiles);
  }

  /**
   * Returns true if no letter tile action has been taken on this turn, or equivalently, if it is
   * still legal to take a letter tile action on this turn.
   *
   * @param player Player who wants to take a letter action.
   * @return true if it is legal to take a letter tile action this turn, false otherwise.
   */
  public boolean canTakeLetterAction(Player player) {
    return !endActionTaken && !letterActionTaken && player.equals(this.player);
  }

  /**
   * Records that a letter tile action, either swapping or placing, has taken place on this turn.
   *
   * @param player Player taking letter action.
   */
  public void takeLetterAction(Player player) {
    letterActionTaken = true;
  }

  /**
   * Returns true if no special tile action has been taken on this turn, or equivalently, if it is
   * still legal to take a special tile action on this turn.
   *
   * @param player Player who wants to take a special tile action.
   * @return true if it is legal to take a special tile action this turn, false otherwise.
   */
  public boolean canTakeSpecialAction(Player player) {
    return !endActionTaken && !specialActionTaken && player.equals(this.player);
  }

  /**
   * Records that a special tile action, either purchasing or placing, has taken place on this turn.
   *
   * @param player Player taking special action.
   */
  public void takeSpecialAction(Player player) {
    specialActionTaken = true;
  }

  /**
   * @param player Player who wants to challenge the current move.
   * @return True if {@code player} is allowed to challenge a move placed on this turn.
   */
  public boolean canTakeChallengeAction(Player player) {
    return !endActionTaken && playedMove != null && !challengeActionTaken && !player.equals(this.player);
  }

  /**
   * Records that a challenge was started on this turn.
   *
   * @param player Player taking challenge action.
   */
  public void takeChallengeAction(Player player) {
    challengeActionTaken = true;
  }

  /**
   * @param player Player who wants to end the current move.
   * @return True if {@code player} is allowed to end this turn.
   */
  public boolean canTakeEndAction(Player player) {
    return player.equals(this.player) && !endActionTaken;
  }

  /**
   * Records that the turn was ended by the player.
   *
   * @param player Player ending their turn.
   */
  public void takeEndAction(Player player) {
    if (this.player.equals(player)) {
      endActionTaken = true;
    }
  }

  /**
   * Associates the given move with being placed on this turn.
   *
   * @param playedMove Move played as part of this turn.
   */
  public void setPlayedMove(Move playedMove) {
    this.playedMove = playedMove;
  }

  /**
   * Returns the move associated with this turn if it exists.
   *
   * @return Move objet played on this turn, or null.
   */
  public Move getPlayedMove() {
    return playedMove;
  }

  /**
   * Returns the player associated with this turn.
   *
   * @return player associated with this turn.
   */
  public Player player() {
    return player;
  }
}
