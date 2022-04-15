package edu.cmu.cs.cs323.scrabble.core.letters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates tiles bags with the default tiles you would see in a Scrabble game.
 */
public class DefaultTileBag {
    private static final List<LetterTile> DEFAULT_TILES = new ArrayList<>();
    static {
        for (Letter letter : Letter.values()) {
            for (int i = 0; i < numberOfTiles(letter); i++) {
                DEFAULT_TILES.add(new LetterTile(letter));
            }
        }
    }

    /**
     * Creates a tile bag with the same tile counts as Scrabble, as well as a custom random object.
     *
     * @param random a (seeded) random object.
     * @return A tile bag instantiaed with the tiles present in a standard scrabble deck and the given random object.
     */
    public static TileBag create(Random random) {
        return new TileBag(DEFAULT_TILES, random);
    }

    /**
     * Creates a tile bag with the same tile counts as Scrabble.
     *
     * @return A tile bag instantiaed with the tiles present in a standard scrabble deck.
     */
    public static TileBag create() {
        return create(new Random());
    }



    /**
     *
     * Returns the number of tiles carrying the letter {@code letter} appear in
     * the bag at the beginning of a game of Scrabble.
     *
     * These default tiles counts sum to 98, not 100, like in the original game,
     * because of the blank tiles. The tile counts here are based on Wikipedia.
     *
     * 1 point: E ×12, A ×9, I ×9, O ×8, N ×6, R ×6, T ×6, L ×4, S ×4, U ×4 2
     * points: D ×4, G ×3 3 points: B ×2, C ×2, M ×2, P ×2 4 points: F ×2, H ×2, V
     * ×2, W ×2, Y ×2 5 points: K ×1 8 points: J ×1, X ×1 10 points: Q ×1, Z ×1
     *
     * @param letter
     *          Letter the client is asking about, in regards to the number of
     *          tiles to add to the default bag.
     *
     */
    private static int numberOfTiles(Letter letter) {
        // CHECKSTYLE:OFF
        switch (letter) {
            case E:
                return 12;
            case A:
            case I:
                return 9;
            case O:
                return 8;
            case N:
            case R:
            case T:
                return 6;
            case L:
            case S:
            case U:
            case D:
                return 4;
            case G:
                return 3;
            case K:
            case J:
            case X:
            case Q:
            case Z:
                return 1;
            default:
                return 2;
        }
        // CHECKSTYLE:ON
    }
}
