package edu.cmu.cs.cs323.scrabble.gui.util;

import edu.cmu.cs.cs323.scrabble.core.board.DefaultBoard;
import edu.cmu.cs.cs323.scrabble.core.board.Position;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Re-usable dialogs for Scrabble With Stuff.
 */
public final class Dialogs {
  private static final String POSITION_PROMPT =
          "Please choose the row and column of the placement position. (Indices start at 1)";
  private static final String POSITION_TITLE = "Pick a Position";
  private static final String ROW_LABEL = "Select a row: ";
  private static final String COL_LABEL = "Select a column: ";
  private static final String NUMBER_PROMPT = "Please select a number:";
  private static final String NUMBER_TITLE = "Select a number";

  /**
   * Prompts the user to select a board position.
   *
   * @return a position on the board if the user selects one, {@code null} if they do not.
   */
  public static Position askForPosition() {
    /* Create options arrays with all possible row and columns on the board. Use indices starting
     * at 1 because that is more normal for people not into CS. */
    JComboBox<Integer> rowOptions = indexedComboBox(1, DefaultBoard.HEIGHT + 1);
    JComboBox<Integer> colOptions = indexedComboBox(1, DefaultBoard.WIDTH + 1);

    /* Add row and column labels to the boxes so players know which one they are selecting. */
    JPanel labeledRowOptions = labelOptions(rowOptions, ROW_LABEL);
    JPanel labeledColOptions = labelOptions(colOptions, COL_LABEL);

    /* Combine row/column dropdown boxes into one options panel. */
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
    optionsPanel.add(new JLabel(POSITION_PROMPT));
    optionsPanel.add(labeledRowOptions);
    optionsPanel.add(labeledColOptions);

    JOptionPane.showMessageDialog(null, optionsPanel, POSITION_TITLE, JOptionPane.PLAIN_MESSAGE);

    return new Position(rowOptions.getSelectedIndex(), colOptions.getSelectedIndex());
  }

  /**
   * Prompts the user to select a number.
   *
   * @param min Inclusive lower bound.
   * @param max Exclusive upper bound.
   * @return Integer in the range [min, max) if one was selected, null otherwise.
   */
  public static Integer askForNumber(int min, int max) {
    return askForNumber(min, max, NUMBER_PROMPT);
  }

  /**
   * Prompts the user to select a number.
   *
   * @param min Inclusive lower bound.
   * @param max Exclusive upper bound.
   * @param prompt Message to display as a prompt.
   * @return Integer in the range [min, max) if one was selected, null otherwise.
   */
  public static Integer askForNumber(int min, int max, String prompt) {
    Integer[] numberOptions = numberArray(min, max);

    return (Integer)JOptionPane.showInputDialog(null, prompt, NUMBER_TITLE,
            JOptionPane.PLAIN_MESSAGE, null, numberOptions, null);
  }

  private static JPanel labelOptions(JComboBox options, String labelString) {
    JPanel panel = new JPanel();

    /* Right align label text so it is pressed against the box. */
    JLabel label = new JLabel(labelString);
    label.setHorizontalTextPosition(JLabel.RIGHT);

    panel.add(label);
    panel.add(options);

    return panel;
  }

  /**
   * @param min Inclusive lower bound.
   * @param max Exclusive upper bound.
   * @return JComboBox with options in the range [min, max)
   */
  private static JComboBox<Integer> indexedComboBox(int min, int max) {
    if (min > max) {
      throw new IllegalArgumentException("min must be greater than max.");
    }
    Integer[] options = numberArray(min, max);
    assert(options[options.length - 1] == max - 1);

    return new JComboBox<>(options);
  }

  /**
   * @param min Inclusive lower bound.
   * @param max Exclusive upper bound.
   * @return Integer[] with sequential values in the range [min, max)
   */
  private static Integer[] numberArray(int min, int max) {
    Integer[] numbers = new Integer[max - min];
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = min + i;
    }
    return numbers;
  }

}
