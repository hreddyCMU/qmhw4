package edu.cmu.cs.cs323.scrabble.core.game;

import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a single player's information for a game of SWS.
 */
public final class Player {
  private final String name;
  private int score = 0;
  private final List<LetterTile> letterTiles = new ArrayList<>();
  private final List<SpecialTile> specialTiles = new ArrayList<>();

  /**
   * Constructor. Creates a player with the given name, initialized to have a score of 0 and no owned tiles.
   *
   * Requires: name != null && name.length > 0
   *
   * @param name
   * Name of player to be created.
   */
  public Player(String name) {
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException("Names must be non-null and contain at least one character.");
    }
    this.name = name;
  }

  /**
   * @return Player's current score. (Note: this may be negative)
   */
  public int getScore() {
    return score;
  }

  /**
   * Sets players score.
   *
   * @param score Value to set player's score to.
   */
  public void setScore(int score) {
    this.score = score;
  }

  public String name() { return name; }

  @Override
  public String toString() {
    return String.format("%s:%d", name, score);
  }

  /**
   * @return The tiles in the player's rack. Mutable.
   */
  public List<LetterTile> letterTiles() {
    return letterTiles;
  }

  /**
   * @return List of special tiles owned by the player. Mutable.
   */
  public List<SpecialTile> specialTiles() {
    return specialTiles;
  }
}
