package com.webforj.builtwithwebforj.terminal.commands;

/**
 * Receives the terminal input while a command has captured it.
 */
public interface InputHandler {

  /**
   * Handles the data the user typed, such as a character or an arrow key sequence.
   *
   * @param data the typed data
   */
  void onInput(String data);

  /**
   * Called when the terminal goes away while the input is still captured.
   */
  default void close() {}
}
