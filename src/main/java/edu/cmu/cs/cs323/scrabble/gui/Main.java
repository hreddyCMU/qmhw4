package edu.cmu.cs.cs323.scrabble.gui;

import edu.cmu.cs.cs323.scrabble.core.game.Player;
import edu.cmu.cs.cs323.scrabble.core.game.SWSGame;
import edu.cmu.cs.cs323.scrabble.gui.util.Dialogs;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 * Runnable Scrabble With Stuff GUI.
 */
public class Main {

  /* Number of players */
  private static final String NUM_PLAYERS_ASK = "Please select the number of players";
  private static final int MIN_PLAYERS = 2;
  private static final int MAX_PLAYERS = 4;
  private static final int NUM_PLAYERS_DEFAULT = MIN_PLAYERS;

  /* Player names */
  private static final String PLAYER_NAME_DEFAULT_FMT = "Player %d";
  private static final String PLAYER_NAME_ASK = "Please enter your name.";


  /**
   * Runs Scrabble With Stuff game with a GUI.
   *
   * @param args Ignored.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      /* First ask the number of players in the game */
      int numPlayers = askNumberOfPlayers();

      /* For each player, ask the participants to enter a name. */
      List<Player> playerList = new ArrayList<>();
      for (int i = 0; i < numPlayers; i++) {
        /* Most people are more comfortable with "Player 1" than "Player 0" so we say the player
         * number is its index + 1. */
        String playerName = askPlayerName(i + 1);
        playerList.add(new Player(playerName));
      }

      /* Offsets between player windows to make it more obvious when it changes between them. */
      int xOffset = 0;
      int xDelta = 100;
      int yOffset = 0;
      int yDelta = 0;
      SWSGame game = new SWSGame(playerList);
      for (Player player : playerList) {
        PlayerFrame playerFrame = new PlayerFrame(game, player, playerList);
        playerFrame.setLocation(xOffset, yOffset);
        playerFrame.setVisible(true);
        xOffset += xDelta;
        yOffset += yDelta;
      }
      game.setupGame();
    });
  }

  /** Ask for client to select the number of players participating. Since we don't want to fail,
   * askNumberOfPlayers guarantees a return value of 2, 3, or 4 regardless of user input. */
  private static int askNumberOfPlayers() {
    Integer playerCount = Dialogs.askForNumber(MIN_PLAYERS, MAX_PLAYERS + 1, NUM_PLAYERS_ASK);
    if (playerCount != null) {
      return playerCount.intValue();
    }
    return NUM_PLAYERS_DEFAULT;
  }

  private static String askPlayerName(int playerNumber) {
    String defaultName = String.format(PLAYER_NAME_DEFAULT_FMT, playerNumber);
    String titleString = String.format("%s:\n    %s", defaultName, PLAYER_NAME_ASK);

    String enteredName = JOptionPane.showInputDialog(titleString, defaultName);

    if (enteredName != null) {
      return enteredName;
    }

    return defaultName;
  }

}
