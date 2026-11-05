package com.webforj.builtwithwebforj.terminal.commands;

import com.webforj.component.optiondialog.OptionDialog;
import java.util.Arrays;

public class MsgCommand implements TerminalCommand {

  @Override
  public String getName() {
    return "msg";
  }

  @Override
  public String getDescription() {
    return "Show a message dialog";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    if (args.length < 2) {
      shell.getTerminal().writeln("\u001B[1;31mUsage: msg <message>\u001B[0m");
      return;
    }

    String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
    OptionDialog.showMessageDialog(message, "Terminal Message");
    shell.getTerminal().writeln("\u001B[1;32mDialog shown!\u001B[0m");
  }
}
