package edu.cmu.cs.cs323.scrabble.core.specialtile.defaults;

import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTileFactory;

/**
 * Factories for the default SWS Special Tiles stored as values.
 */
public enum DefaultSpecialTileFactories implements SpecialTileFactory {
  NEGATIVE_POINTS {
    @Override public String identifier() { return NegativePointsSpecialTile.identifier(); }
    @Override public SpecialTile create(Player player) { return new NegativePointsSpecialTile(player); }
  },
  REVERSE {
    @Override public String identifier() { return ReverseSpecialTile.identifier(); }
    @Override public SpecialTile create(Player player) { return new ReverseSpecialTile(player); }
  },
  BOOM {
    @Override public String identifier() { return BoomSpecialTile.identifier(); }
    @Override public SpecialTile create(Player player) { return new BoomSpecialTile(player); }
  },
  STEAL_SCORE {
    @Override public String identifier() { return StealScoreSpecialTile.identifier(); }
    @Override public SpecialTile create(Player player) { return new StealScoreSpecialTile(player); }
  },
  SKIP_TURN {
    @Override public String identifier() { return SkipTurnSpecialTile.identifier(); }
    @Override public SpecialTile create(Player player) { return new SkipTurnSpecialTile(player); }
  };
}
