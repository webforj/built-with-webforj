package com.webforj.builtwithwebforj.terminal.commands;

public interface TerminalCommand {

  /**
   * Get the command name.
   *
   * @return the command name
   */
  String getName();

  /**
   * Get the command description.
   *
   * @return the command description
   */
  String getDescription();

  /**
   * Execute the command.
   *
   * @param shell the shell the command runs in
   * @param args the command name followed by its arguments
   */
  void execute(Shell shell, String[] args);
}
