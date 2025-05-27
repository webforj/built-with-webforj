package com.webforj.tictactoe.core;

/**
 * Represents the state of the game.
 */
public class GameState {
  private final String[] board;
  private final Player currentPlayer;
  private final GameResult status;
  private final int[] winningPattern;

  /**
   * Constructs a new GameState with the specified parameters.
   *
   * @param board          the current state of the board
   * @param currentPlayer  the current player
   * @param result         the current result of the game
   * @param winningPattern the indices of the winning pattern
   */
  public GameState(String[] board, Player currentPlayer, GameResult result,
      int[] winningPattern) {
    this.board = board.clone();
    this.currentPlayer = currentPlayer;
    this.status = result;
    this.winningPattern = winningPattern.clone();
  }

  /**
   * Returns the current state of the board.
   *
   * @return the current state of the board
   */
  public String[] getBoard() {
    return board.clone();
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
   * Returns the current result of the game.
   *
   * @return the current result of the game
   */
  public GameResult getResult() {
    return status;
  }

  /**
   * Returns the indices of the winning pattern.
   *
   * @return the indices of the winning pattern
   */
  public int[] getWinningPattern() {
    return winningPattern.clone();
  }
}
