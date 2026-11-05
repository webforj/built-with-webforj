package com.webforj.builtwithwebforj.terminal.commands;

import com.webforj.component.terminal.Terminal;

/**
 * The terminal session a command runs in.
 */
public interface Shell {

  /**
   * Returns the terminal the command writes to.
   *
   * @return the terminal
   */
  Terminal getTerminal();

  /**
   * Sends all input to the given handler until {@link #releaseInput()} is called. The shell shows
   * no prompt while the input is captured.
   *
   * @param handler the handler that receives the input
   */
  void captureInput(InputHandler handler);

  /**
   * Gives the input back to the shell and shows the prompt.
   */
  void releaseInput();
}
