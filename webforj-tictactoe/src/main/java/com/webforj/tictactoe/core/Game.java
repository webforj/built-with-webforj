package com.webforj.tictactoe.core;

import java.util.ArrayList;
import java.util.List;

import com.webforj.dispatcher.EventDispatcher;
import com.webforj.dispatcher.EventListener;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.tictactoe.core.event.GameStateChangeEvent;

/**
 * A simple Tic Tac Toe Game.
 */
public class Game {
  private final EventDispatcher dispatcher = new EventDispatcher();
  private final String[] board = new String[9];
  private Player playerX;
  private Player playerO;
  private Player currentPlayer;
  private GameResult gameResult = GameResult.CONTINUE;
  private int[] winningPattern = new int[0];

  /**
   * Constructs a new GameEngine with the specified player names.
   *
   * @param playerXName the name of player X
   * @param playerOName the name of player O
   */
  public Game(String playerXName, String playerOName) {
    this.playerX = new Player(playerXName, "X");
    this.playerO = new Player(playerOName, "O");
    reset();
  }

  /**
   * Returns player X.
   *
   * @return player X
   */
  public Player getPlayerX() {
    return playerX;
  }

  /**
   * Returns player O.
   *
   * @return player O
   */
  public Player getPlayerO() {
    return playerO;
  }

  /**
   * Returns the current player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Makes a move at the specified index.
   *
   * @param index the index of the move
   * @return the result of the move
   */
  public GameResult makeMove(int index) {
    if (gameResult != GameResult.CONTINUE || index < 0 || index >= 9 || !board[index].isEmpty()) {
      return GameResult.INVALID;
    }

    board[index] = currentPlayer.getSymbol();

    if (isWin(currentPlayer)) {
      gameResult = GameResult.WIN;
      winningPattern = getWinningPatternIndices(currentPlayer);
      update();

      return GameResult.WIN;
    }

    if (isDraw()) {
      gameResult = GameResult.DRAW;

      update();
      return GameResult.DRAW;
    }

    currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    update();

    return GameResult.CONTINUE;
  }

  /**
   * Resets the game to its initial state.
   */
  public void reset() {
    for (int i = 0; i < 9; i++) {
      board[i] = "";
    }

    currentPlayer = playerX;
    gameResult = GameResult.CONTINUE;
    winningPattern = new int[0];
    update();
  }

  /**
   * Returns the current state of the game.
   *
   * @return the current state
   */
  public GameState getState() {
    return new GameState(board, currentPlayer, gameResult, winningPattern);
  }

  /**
   * Returns the winning pattern indices.
   *
   * @return the winning pattern indices
   */
  public List<Integer> getWinningPattern() {
    List<Integer> pattern = new ArrayList<>();
    for (int index : winningPattern) {
      pattern.add(index);
    }

    return pattern;
  }

  /**
   * Adds a listener for game state change events.
   *
   * @param listener the listener to add
   * @return the listener registration
   */
  public ListenerRegistration<GameStateChangeEvent> addGameStateChangedListener(
      EventListener<GameStateChangeEvent> listener) {
    return dispatcher.addListener(GameStateChangeEvent.class, listener);
  }

  /**
   * Sends a game state change event to all registered listeners.
   */
  public void update() {
    dispatcher.dispatchEvent(new GameStateChangeEvent(this));
  }

  private int[] getWinningPatternIndices(Player player) {
    int[][] winningCombinations = {
        { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // Rows
        { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // Columns
        { 0, 4, 8 }, { 2, 4, 6 } // Diagonals
    };

    for (int[] combo : winningCombinations) {
      if (board[combo[0]].equals(player.getSymbol()) &&
          board[combo[1]].equals(player.getSymbol()) &&
          board[combo[2]].equals(player.getSymbol())) {
        return combo;
      }
    }

    return new int[0];
  }

  private boolean isWin(Player player) {
    return getWinningPatternIndices(player).length > 0;
  }

  private boolean isDraw() {
    for (String cell : board) {
      if (cell.isEmpty()) {
        return false;
      }
    }

    return true;
  }
}
