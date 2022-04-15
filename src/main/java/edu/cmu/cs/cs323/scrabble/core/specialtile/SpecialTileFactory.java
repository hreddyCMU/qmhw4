package edu.cmu.cs.cs323.scrabble.core.specialtile;

import edu.cmu.cs.cs323.scrabble.core.game.Player;

/**
 * Creates instances of Special Tiles. These methods could be static methods of
 * SpecialTile itself, but Java interfaces do not allow for specification of
 * statics.
 */
public interface SpecialTileFactory {
  /**
   * Unique and descriptive tile identifier. Implementer of subclasses is
   * responsible for ensuring identifier is relatively specific, but the client
   * must ultimately ensure they do not use multiple subclasses with the same
   * identifier.
   *
   * @return Unique identifier which describes the tile.
   */
  String identifier();

  /**
   * Creates a SpecialTile instance whose action corresponds to the identifier.
   *
   * @param player Owner of the tile.
   * @return new SpecialTile instance.
   */
  SpecialTile create(Player player);
}
