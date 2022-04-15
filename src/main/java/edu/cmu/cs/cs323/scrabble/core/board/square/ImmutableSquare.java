package edu.cmu.cs.cs323.scrabble.core.board.square;

import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable representation of a Square object.
 */
public class ImmutableSquare implements Square {
  private static final String IMMUTABLE = "Immutable Square";

  private final int wordMultiplier;
  private final int letterMultiplier;
  private final LetterTile letterTile;
  private final List<SpecialTile> specialTiles;

  /**
   * Constructor. Creates an immutable square with the given properties.
   *
   * @param wordMultiplier Coefficient for word score modifier.
   * @param letterMultiplier Coefficient for letter score modifier.
   * @param letterTile Letter tile on square (possibly null).
   * @param specialTiles Non-null list of special tiles.
   */
  public ImmutableSquare(int wordMultiplier, int letterMultiplier, LetterTile letterTile, List<SpecialTile> specialTiles) {
    this.wordMultiplier = wordMultiplier;
    this.letterMultiplier = letterMultiplier;
    this.letterTile = letterTile;
    this.specialTiles = new ArrayList<SpecialTile>(specialTiles);
  }

  @Override
  public int wordMultiplier() {
    return wordMultiplier;
  }

  @Override
  public int letterMultiplier() {
    return letterMultiplier;
  }

  @Override
  public List<SpecialTile> getSpecialTiles() {
    return new ArrayList<>(specialTiles);
  }

  @Override
  public boolean hasSpecialTiles() {
    return specialTiles.size() > 0;
  }

  @Override
  public boolean hasLetterTile() {
    return letterTile != null;
  }

  @Override
  public LetterTile getLetterTile() {
    return letterTile;
  }

  /* Mutator methods from Square interface */

  @Override
  public void clearSpecialTiles() {
    throw new UnsupportedOperationException(IMMUTABLE);
  }

  @Override
  public void placeSpecialTile(SpecialTile tileToPlace) {
    throw new UnsupportedOperationException(IMMUTABLE);
  }

  @Override
  public void placeLetterTile(LetterTile tileToPlace) {
    throw new UnsupportedOperationException(IMMUTABLE);
  }

  @Override
  public LetterTile removeLetterTile() {
    throw new UnsupportedOperationException(IMMUTABLE);
  }
}
