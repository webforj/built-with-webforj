package com.webforj.builtwithwebforj.terminal.commands;

import com.webforj.component.terminal.Terminal;
import java.util.Map;

public class HelpCommand implements TerminalCommand {

  private final Map<String, TerminalCommand> commands;

  public HelpCommand(Map<String, TerminalCommand> commands) {
    this.commands = commands;
  }

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Show available commands";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    Terminal term = shell.getTerminal();
    term.writeln("\u001B[1;36mAvailable Commands:\u001B[0m");
    term.writeln("");
    for (TerminalCommand command : commands.values()) {
      term.writeln("  \u001B[1;33m" + String.format("%-12s", command.getName()) + "\u001B[0m - "
          + command.getDescription());
    }
    term.writeln("");
  }
}
