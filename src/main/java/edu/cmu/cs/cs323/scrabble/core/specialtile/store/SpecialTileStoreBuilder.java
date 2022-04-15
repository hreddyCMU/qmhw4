package edu.cmu.cs.cs323.scrabble.core.specialtile.store;

import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTileFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder object to create new SpecialTileStore objects.
 *
 * Pretty much unnecessary, but I wanted to try the pattern.
 */
public class SpecialTileStoreBuilder {
  private static final String LOW_PRICE = "Price of special tile must be > 0.";
  private static final String DUPLICATE_ID = "Identifier has already been registered.";
  private static final String DNE = "This factory was not registered before.";
  private final Map<String, SpecialTileFactory> factories = new HashMap<>();
  private final Map<String, Integer> prices = new HashMap<>();

  /**
   * Adds a factory to the tile store's list of offerings, with the associated price.
   *
   * @param factory
   * Tile factory to be registered.
   * @param price
   * Price that should be charged for tiles created with the factory.
   */
  public void registerSpecialTileFactory(SpecialTileFactory factory, int price) {
    if (hasSpecialTile(factory.identifier())) {
      throw new IllegalArgumentException(DUPLICATE_ID);
    }
    if (price < 1) {
      throw new IllegalArgumentException(LOW_PRICE);
    }
    factories.put(factory.identifier(), factory);
    prices.put(factory.identifier(), price);
  }

  /**
   * Removes a factory to the tile store's list of offerings.
   *
   * @param factory
   * Tile factory to be registered.
   */
  public void unregisterSpecialTileFactory(SpecialTileFactory factory) {
    if (!hasSpecialTile(factory.identifier())) {
      throw new IllegalArgumentException(DNE);
    }
    factories.remove(factory.identifier());
    prices.remove(factory.identifier());
  }

  /**
   * Create a SpecialTileStore using the factories registered thus far.
   *
   * @return SpecialTileStore with the registered factories.
   */
  public SpecialTileStore construct() {
    return new SpecialTileStore(factories, prices);
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

  private SpecialTileFactory factoryForIdentifier(String specialTileIdentifier) {
    return factories.get(specialTileIdentifier);
  }

}
