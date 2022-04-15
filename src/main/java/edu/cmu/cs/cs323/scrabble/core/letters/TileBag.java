package edu.cmu.cs.cs323.scrabble.core.letters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Stores tiles that are not in play or on a player's rack. Players use the tile
 * bag to draw new tiles, or swap their current tiles for new ones.
 */
public final class TileBag {
  public static final int MAX_SWAP = 7;
  private final List<LetterTile> tiles;
  private final Random random;

  /**
   * Constructor. Allows the client to set their own random.
   *
   * @param tiles tiles to instantiate the bag with.
   * @param random (seeded) random object.
   */
  public TileBag(List<LetterTile> tiles, Random random) {
    this.tiles = new ArrayList<>(tiles);
    this.random = random;
  }

  /**
   * Instantiates a tile bag with the default set of tiles used to start a
   * standard scrabble game (less the two blank tiles, which are unsupported).
   *
   * @param tiles tiles to instantiate the bag with.
   */
  public TileBag(List<LetterTile> tiles) {
    this(tiles, new Random());
  }

  /**
   * Determines if it is allowable to swap tiles with the tile bag, which per Scrabble rules must
   * have at least 7 tiles for a swap to occur.
   *
   * @return true if tiles can be swapped, false otherwise
   */
  public boolean canSwapTiles() {
    return tiles.size() >= MAX_SWAP;
  }

  /**
   * Swaps the given tiles with new tiles from the tile bags. This is achieved by adding the given
   * tiles into the bag, and returning a list of new tiles.
   *
   * Requires: {@code canSwapTiles()}
   *
   * @param oldTiles Tiles to be swapped.
   * @return List of new tiles drawn from the bag.
   */
  public List<LetterTile> swapTiles(List<LetterTile> oldTiles) {
    if (!canSwapTiles()) {
      throw new IllegalArgumentException("Cannot swap tiles");
    }
    if (oldTiles.size() == 0) {
      throw new IllegalArgumentException("Must swap at least one tile");
    }
    if (oldTiles.size() > MAX_SWAP) {
      throw new IllegalArgumentException("Cannot swap more than MAX_SWAP tiles");
    }
    /* Get the new tiles from the bag before adding the old tiles to it. Otherwise, you could
     * end up with the same tiles.
     */
    List<LetterTile> newTiles = new ArrayList<>();
    for (int i = 0; i < oldTiles.size(); i++) {
      newTiles.add(drawTile());
    }
    /* Add the input tiles into the bag. */
    tiles.addAll(oldTiles);
    return newTiles;
  }


  /**
   * Used to determine whether or not there are more tiles to be drawn from the
   * bag.
   *
   * @return true if there are not tiles left to draw, false otherwise.
   */
  public boolean isEmpty() {
    return tiles.size() == 0;
  }

  /**
   * Removes and returns a random tile from the tile bag. Requires the bag is
   * not empty (see {@code isEmpty()}). If a draw is attempted when the bag is
   * empty, an exception will be thrown.
   *
   * @return a randomly selected tile from the bag
   */
  public LetterTile drawTile() {
    return tiles.remove(random.nextInt(tiles.size()));
  }
}
