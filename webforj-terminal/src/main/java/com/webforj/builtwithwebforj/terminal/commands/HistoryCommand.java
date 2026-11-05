package com.webforj.builtwithwebforj.terminal.commands;

import com.webforj.component.terminal.Terminal;
import java.util.List;

public class HistoryCommand implements TerminalCommand {

  private final List<String> history;

  public HistoryCommand(List<String> history) {
    this.history = history;
  }

  @Override
  public String getName() {
    return "history";
  }

  @Override
  public String getDescription() {
    return "Show command history";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    Terminal term = shell.getTerminal();
    if (history.isEmpty()) {
      term.writeln("\u001B[1;33mNo command history yet\u001B[0m");
      return;
    }

    term.writeln("\u001B[1;36mCommand History:\u001B[0m");
    for (int i = 0; i < history.size(); i++) {
      term.writeln(String.format("  \u001B[1;33m%3d\u001B[0m  %s", i + 1, history.get(i)));
    }
  }
}
