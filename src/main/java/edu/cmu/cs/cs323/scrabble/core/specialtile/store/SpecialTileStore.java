package edu.cmu.cs.cs323.scrabble.core.specialtile.store;

import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTileFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Allows players to "buy" special tiles using points.
 * 
 * The store keeps a list of the available tiles, and their associated costs,
 * which can be queried by players.
 */
public final class SpecialTileStore {
  private static final String ILLEGAL_ID = "Identifier Not Found.";
  private final Map<String, SpecialTileFactory> factories;
  private final Map<String, Integer> prices;


  /**
   * Package private constructor to be used with the SpecialTileStoreBuidler.
   *
   * @param factories Map of identifiers to factory objects.
   * @param prices Map of identifiers to prices.
   */
  SpecialTileStore(Map<String, SpecialTileFactory> factories, Map<String, Integer> prices) {
    this.factories = Collections.unmodifiableMap(factories);
    this.prices = Collections.unmodifiableMap(prices);
  }


  /**
   * Determines whether a special tile is for sale with the given identifier.
   *
   * @param specialTileIdentifier Identifier of a special tile.
   * @return true if the specified identifier corresponds to a tile that is for sale.
   */
  public boolean hasSpecialTile(String specialTileIdentifier) {
    return factoryForIdentifier(specialTileIdentifier) != null;
  }

  /**
   * Retrieves the cost of purchasing a tile with the given identifier.
   *
   * Requires @{code hasSpecialTile(specialTileIdentifier)}
   *
   * @param specialTileIdentifier Identifier of the special tile to be purchased.
   * @return Price for the special tile in question.
   */
  public int priceOfSpecialTile(String specialTileIdentifier) {
    if (!hasSpecialTile(specialTileIdentifier)) {
      throw new IllegalArgumentException(ILLEGAL_ID);
    }
    return prices.get(specialTileIdentifier);
  }

  /**
   * Charges the given player for the desired tile, and adds it to their list of
   * special tiles.
   *
   * //@requires hasSpecialTile(specialTileIdentifier) && player.getScore() >=
   * priceOfSpecialTile(specialTileIdentifier)
   *
   * @param player
   *          Player purchasing tile.
   * @param specialTileIdentifier
   *          Unique identifier of tile to be purchased.
   */
  public void buySpecialTile(Player player, String specialTileIdentifier) {
    if (!hasSpecialTile(specialTileIdentifier)) {
      throw new IllegalArgumentException(ILLEGAL_ID);
    }
    SpecialTileFactory factory = factories.get(specialTileIdentifier);
    int price = prices.get(specialTileIdentifier);
    player.setScore(player.getScore() + price);
    player.specialTiles().add(factory.create(player));
  }

  /**
   * Retrieves a list of identifiers for the tiles available in this tile store.
   *
   * @return list of tile identifiers for all tiles that can be purchased.
   */
  public List<String> purchasableTileDescriptions() {
    return new ArrayList<>(factories.keySet());
  }

  private SpecialTileFactory factoryForIdentifier(String specialTileIdentifier) {
    return factories.get(specialTileIdentifier);
  }

}
