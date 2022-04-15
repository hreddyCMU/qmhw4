package edu.cmu.cs.cs323.scrabble.core.board.square;

import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the most common type of square on a scrabble board, that is one whose multipliers
 * have no effect on the score of a word.
 */
abstract class SquareImpl implements Square {
  /*
   * There may be more than one special tile on a single square, because each
   * player has different views of the board, so may place their tiles on the
   * same square, believing it to be empty.
   */
  private final List<SpecialTile> specialTiles = new ArrayList<>();
  private LetterTile letterTile;

  @Override
  public List<SpecialTile> getSpecialTiles() {
    return new ArrayList<SpecialTile>(specialTiles);
  }

  @Override
  public void clearSpecialTiles() {
    specialTiles.clear();
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
  public void placeSpecialTile(SpecialTile tileToPlace) {
    specialTiles.add(tileToPlace);
  }

  @Override
  public void placeLetterTile(LetterTile tileToPlace) {
    letterTile = tileToPlace;
  }

  @Override
  public LetterTile getLetterTile() {
    return letterTile;
  }

  @Override
  public LetterTile removeLetterTile() {
    LetterTile oldTile = letterTile;
    letterTile = null;
    return oldTile;
  }
}
