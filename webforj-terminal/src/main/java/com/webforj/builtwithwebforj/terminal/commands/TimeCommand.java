package com.webforj.builtwithwebforj.terminal.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements TerminalCommand {

  private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public String getName() {
    return "time";
  }

  @Override
  public String getDescription() {
    return "Display current date and time";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    shell.getTerminal()
        .writeln("\u001B[1;32mCurrent time: " + LocalDateTime.now().format(FORMAT) + "\u001B[0m");
  }
}
