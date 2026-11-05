package com.webforj.builtwithwebforj.terminal.commands;

public class ClearCommand implements TerminalCommand {

  @Override
  public String getName() {
    return "clear";
  }

  @Override
  public String getDescription() {
    return "Clear the terminal screen";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    shell.getTerminal().clear();
  }
}
