package edu.cmu.cs.cs323.scrabble.core.board;


import edu.cmu.cs.cs323.scrabble.core.board.square.DoubleLetterScoreSquare;
import edu.cmu.cs.cs323.scrabble.core.board.square.DoubleWordScoreSquare;
import edu.cmu.cs.cs323.scrabble.core.board.square.Square;
import edu.cmu.cs.cs323.scrabble.core.board.square.StandardSquare;
import edu.cmu.cs.cs323.scrabble.core.board.square.TripleLetterScoreSquare;
import edu.cmu.cs.cs323.scrabble.core.board.square.TripleWordScoreSquare;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to hold data for the default scrabble board. This is more of an organziational tool, because we don't want to
 * put this much clutter in the Board class.
 */
public final class DefaultBoard {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;

  /**
   * Creates a default SWS board. This board is the same size and has the same scoremodifiers as
   * a regular Scrabble board.
   *
   * @return Default SWS board.
   */
  public static Board create() {
        Map<Position, Square> mapping = new HashMap<>();
        Set<Position> doubleLetter = doubleLetterPositions();
        Set<Position> tripleLetter = tripleLetterPositions();
        Set<Position> doubleWord = doubleWordPositions();
        Set<Position> tripleWord = tripleWordPositions();

        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                Position position = new Position(r,c);
                Square square;
                if (doubleLetter.contains(position)) {
                    square = new DoubleLetterScoreSquare();
                } else if (tripleLetter.contains(position)) {
                    square = new TripleLetterScoreSquare();
                } else if (doubleWord.contains(position)) {
                    square = new DoubleWordScoreSquare();
                } else if (tripleWord.contains(position)) {
                    square = new TripleWordScoreSquare();
                } else {
                    square = new StandardSquare();
                }
                mapping.put(position, square);
            }
        }

        Board board = new Board(WIDTH, HEIGHT, mapping);
        return board;
    }

    /**
     * @return All board positions containing double letter score modifiers.
     */
    private static Set<Position> doubleLetterPositions() {
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(0, 3));
        positions.add(new Position(0, 11));
        positions.add(new Position(2, 6));
        positions.add(new Position(2, 8));
        positions.add(new Position(3, 0));
        positions.add(new Position(3, 7));
        positions.add(new Position(3, 14));
        positions.add(new Position(6, 2));
        positions.add(new Position(6, 6));
        positions.add(new Position(6, 8));
        positions.add(new Position(6, 12));
        positions.add(new Position(7, 3));
        positions.add(new Position(7, 11));
        positions.add(new Position(8, 2));
        positions.add(new Position(8, 6));
        positions.add(new Position(8, 8));
        positions.add(new Position(8, 12));
        positions.add(new Position(11, 0));
        positions.add(new Position(11, 7));
        positions.add(new Position(11, 14));
        positions.add(new Position(12, 6));
        positions.add(new Position(12, 8));
        positions.add(new Position(14, 3));
        positions.add(new Position(14, 11));
        return positions;
    }

    /**
     * @return All board positions containing triple letter score modifiers.
     */
    private static Set<Position> tripleLetterPositions() {
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(1, 5));
        positions.add(new Position(1, 9));
        positions.add(new Position(5, 1));
        positions.add(new Position(5, 5));
        positions.add(new Position(5, 9));
        positions.add(new Position(5, 13));
        positions.add(new Position(9, 1));
        positions.add(new Position(9, 5));
        positions.add(new Position(9, 9));
        positions.add(new Position(9, 13));
        positions.add(new Position(13, 5));
        positions.add(new Position(13, 9));
        return positions;
    }

    /**
     * @return All board positions containing double word score modifiers.
     */
    private static Set<Position> doubleWordPositions() {
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 3));
        positions.add(new Position(4, 4));
        positions.add(new Position(7, 7));
        positions.add(new Position(10, 10));
        positions.add(new Position(11, 11));
        positions.add(new Position(12, 12));
        positions.add(new Position(13, 13));
        positions.add(new Position(10, 4));
        positions.add(new Position(11, 3));
        positions.add(new Position(12, 2));
        positions.add(new Position(13, 1));
        positions.add(new Position(1, 13));
        positions.add(new Position(2, 12));
        positions.add(new Position(3, 11));
        positions.add(new Position(4, 10));
        return positions;
    }

  /**
   * @return All board positions containing triple word score modifiers.
   */
  private static Set<Position> tripleWordPositions() {
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(0, 7));
        positions.add(new Position(0, 14));
        positions.add(new Position(7, 0));
        positions.add(new Position(7, 14));
        positions.add(new Position(14, 0));
        positions.add(new Position(14, 7));
        positions.add(new Position(14, 14));
        return positions;
    }
}
