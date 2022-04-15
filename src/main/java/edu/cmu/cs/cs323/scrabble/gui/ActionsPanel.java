package edu.cmu.cs.cs323.scrabble.gui;

import edu.cmu.cs.cs323.scrabble.core.board.Position;
import edu.cmu.cs.cs323.scrabble.core.game.Move;
import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.core.game.Turn;
import edu.cmu.cs.cs323.scrabble.core.letters.LetterTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.SpecialTile;
import edu.cmu.cs.cs323.scrabble.core.specialtile.store.SpecialTileStore;
import edu.cmu.cs.cs323.scrabble.gui.util.ColorScheme;
import edu.cmu.cs.cs323.scrabble.gui.util.Dialogs;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays the possible actions a user can take at a given time. All actions will be visible at all
 * times, but not all will be "enabled" at all times.
 */
public class ActionsPanel extends JPanel {
  private static final int NUM_BUTTONS = 6;
  private static final String TITLE = "Actions";
  private static final String SWAP_TEXT = "Swap Letters";
  private static final String PLACE_LETTER_TEXT = "Place Letters";
  private static final String BUY_SPECIAL_TEXT = "Buy Special";
  private static final String PLACE_SPECIAL_TEXT = "Place Special";
  private static final String END_TEXT = "End Turn";
  private static final String CHALLENGE_TEXT = "Challenge";
  private static final String ERROR_NO_SPECIAL = "No tiles for sale :/";
  private static final String BUY_SPECIAL_PROMPT_FMT =
          "You can purchase any special tile that costs less than %d points.";
  private static final String PLACE_SPECIAL_PROMPT = "Choose a Special Tile and Location";
  private static final String NUM_TILES_PROMPT = "How many tiles would you like to place?";
  private static final String CHOOSE_LETTER_PROMPT =
          "Pick which letter you would like to place next:";
  private static final String CHOOSE_LETTER_TITLE = "Choose Letter";
  private final JButton swapButton = new JButton(SWAP_TEXT);
  private final JButton placeLetterButton = new JButton(PLACE_LETTER_TEXT);
  private final JButton buySpecialButton = new JButton(BUY_SPECIAL_TEXT);
  private final JButton placeSpecialButton = new JButton(PLACE_SPECIAL_TEXT);
  private final JButton endTurnButton = new JButton(END_TEXT);
  private final JButton challengeButton = new JButton(CHALLENGE_TEXT);
  private final Player player;
  private final SWSGame game;

  /**
   * Creates an action panel attached to the given player.
   *
   * @param game Scrabble With Stuff game.
   * @param player Player from game whose actions to display.
   */
  public ActionsPanel(SWSGame game, Player player) {
    this.player = player;
    this.game = game;

    setBackground(ColorScheme.BOARD_BG);
    TitledBorder titledBorder = BorderFactory.createTitledBorder(TITLE);
    titledBorder.setTitleColor(ColorScheme.SCORE_FG);
    setBorder(titledBorder);
    setLayout(new GridLayout(NUM_BUTTONS, 1));

    /* Start all buttons as disabled until a new turn begins. */
    swapButton.setEnabled(false);
    placeLetterButton.setEnabled(false);
    buySpecialButton.setEnabled(false);
    placeSpecialButton.setEnabled(false);
    endTurnButton.setEnabled(false);
    challengeButton.setEnabled(false);

    /* Add all buttons, grouping by functionality. */
    add(swapButton);
    add(placeLetterButton);
    add(buySpecialButton);
    add(placeSpecialButton);
    add(endTurnButton);
    add(challengeButton);

    swapButton.addActionListener(ae -> {
      /* Sort the tiles so duplicates will appear next to each other. */
      List<LetterTile> sortedTiles = player.letterTiles();
      sortedTiles.sort((LetterTile l1, LetterTile l2) -> l1.toString().compareTo(l2.toString()));

      /* Create a checkbox associated with each letter tile. The user will be able to check which
       * tiles they want to swap and which ones they don't. */
      Map<JCheckBox,LetterTile> checkBoxes = new HashMap<>();
      JPanel checkBoxPanel = new JPanel();
      for (LetterTile letterTile : sortedTiles){
        JCheckBox box = new JCheckBox(letterTile.toString(), true);
        checkBoxes.put(box, letterTile);
        checkBoxPanel.add(box);
      }

      if (JOptionPane.showConfirmDialog(null, checkBoxPanel, SWAP_TEXT,
              JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
        /* Create a list of the selected tiles, and send it to the game to swap. */
        List<LetterTile> toSwap = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxes.keySet()) {
          if (checkBox.isSelected()) {
            toSwap.add(checkBoxes.get(checkBox));
          }
        }
        game.swapTiles(player, toSwap);
      }
    });

    placeLetterButton.addActionListener(ae -> {
      Integer numTiles = Dialogs.askForNumber(1, player.letterTiles().size() + 1, NUM_TILES_PROMPT);
      if (numTiles != null) {
        List<LetterTile> remainingLetters = new ArrayList<>(player.letterTiles());
        Map<Position, LetterTile> placements = new HashMap<>();
        for (int i = 0; i < numTiles; i++) {
          LetterTile letterTile = selectLetterTile(remainingLetters);
          if (letterTile == null) {
            return;
          }
          Position position = Dialogs.askForPosition();
          if (position == null) {
            return;
          }
          placements.putIfAbsent(position, letterTile);
          remainingLetters.remove(letterTile);
        }
        Move move = new Move(placements);
        game.placeMove(player, move);
      }
    });

    buySpecialButton.addActionListener(ae -> {
      SpecialTileStore tileStore = game.tileStore();
      List<String> identifiers = tileStore.purchasableTileDescriptions();

      if (identifiers.size() > 0) {
        /* Create an Object[] that can be used as the options in a dropdown box. Each option should
         * have a description of a special tile as well as its price. */
        Object[] options = new Object[identifiers.size()];
        for (int i = 0; i < options.length; i++) {
          options[i] = String.format("%s - %d pts", identifiers.get(i),
                  tileStore.priceOfSpecialTile(identifiers.get(i)));
        }

        /* Include the player's score in the prompt so they no what tiles they can afford. In the
         * future, it may be nice to have the store only retrieve a list of values that could be
         * purchased with a given number of points. */
        String prompt = String.format(BUY_SPECIAL_PROMPT_FMT, player.getScore());
        String selection = (String)JOptionPane.showInputDialog(null, prompt, BUY_SPECIAL_TEXT,
                JOptionPane.PLAIN_MESSAGE, null, options, null);

        /* If the identifier is non-null, attempt to purchase it. If it is null, they must have
         * cancelled the transaction, so it is acceptable not to do anything. */
        if (selection != null) {
          /* This weird for loop is to deal with the Object[] wrapper required by JOptionPane and
           * the extra information we added to the end of the option descriptions. */
          for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selection)) {
              game.buySpecialTile(player, identifiers.get(i));
              return;
            }
          }
        }
      } else {
        /* Display error if there are no special tiles to purchase. */
        JOptionPane.showMessageDialog(null, ERROR_NO_SPECIAL, BUY_SPECIAL_TEXT,
                JOptionPane.ERROR_MESSAGE);
      }
    });

    placeSpecialButton.addActionListener(ae -> {
      List<SpecialTile> specialTiles = player.specialTiles();

      if (specialTiles.size() > 0) {
        /* Create an Object[] that can be used as the options in a dropdown box. */
        Object[] options = specialTiles.toArray();

        /* First let the player choose which tile they want to place. */
        SpecialTile selection = (SpecialTile)JOptionPane.showInputDialog(null, PLACE_SPECIAL_PROMPT,
                PLACE_SPECIAL_TEXT, JOptionPane.PLAIN_MESSAGE, null, options, null);

        /* If the identifier is non-null, attempt to place the tile. If it is null, they must have
         * cancelled the dialog, so it is acceptable not to do anything. */
        if (selection != null) {
          Position position = Dialogs.askForPosition();
          if (position != null) {
            game.placeSpecialTile(player, position, selection);
          }
        }
      } else {
        /* Display error if there are no special tiles to purchase. */
        JOptionPane.showMessageDialog(null, ERROR_NO_SPECIAL, BUY_SPECIAL_TEXT,
                JOptionPane.ERROR_MESSAGE);
      }
    });

    endTurnButton.addActionListener(ae -> game.endTurn(player));

    challengeButton.addActionListener(ae -> game.challengeLastMove(player));

  }

  /**
   * Updates possible actions based on the given turn.
   *
   * @param turn turn holding new information.
   */
  public void update(Turn turn) {
    swapButton.setEnabled(game.canSwapTiles(player));
    placeLetterButton.setEnabled(turn.canTakeLetterAction(player));
    buySpecialButton.setEnabled(turn.canTakeSpecialAction(player));
    placeSpecialButton.setEnabled(turn.canTakeSpecialAction(player));
    endTurnButton.setEnabled(turn.canTakeEndAction(player));
    challengeButton.setEnabled(turn.canTakeChallengeAction(player));
  }

  private LetterTile selectLetterTile(List<LetterTile> letterTiles) {
    Object[] options = letterTiles.toArray();
    return (LetterTile)JOptionPane.showInputDialog(null, CHOOSE_LETTER_PROMPT, CHOOSE_LETTER_TITLE,
            JOptionPane.PLAIN_MESSAGE, null, options, null);
  }
}
