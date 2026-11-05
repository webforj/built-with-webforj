package com.webforj.builtwithwebforj.terminal.commands;

import com.webforj.component.optiondialog.InputDialog;
import com.webforj.component.optiondialog.OptionDialog;
import java.util.Arrays;

public class PromptCommand implements TerminalCommand {

  @Override
  public String getName() {
    return "prompt";
  }

  @Override
  public String getDescription() {
    return "Prompt the user for input";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    if (args.length < 2) {
      shell.getTerminal().writeln("\u001B[1;31mUsage: prompt <question>\u001B[0m");
      return;
    }

    String question = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
    String answer = OptionDialog.showInputDialog(question, "Terminal Prompt",
        InputDialog.MessageType.QUESTION);

    if (answer != null && !answer.isEmpty()) {
      shell.getTerminal().writeln("\u001B[1;32mYou answered: " + answer + "\u001B[0m");
    } else {
      shell.getTerminal().writeln("\u001B[1;33mNo input provided\u001B[0m");
    }
  }
}
