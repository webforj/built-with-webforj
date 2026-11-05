package com.webforj.builtwithwebforj.terminal.views;

import com.webforj.builtwithwebforj.terminal.commands.ClearCommand;
import com.webforj.builtwithwebforj.terminal.commands.HelpCommand;
import com.webforj.builtwithwebforj.terminal.commands.HistoryCommand;
import com.webforj.builtwithwebforj.terminal.commands.InputHandler;
import com.webforj.builtwithwebforj.terminal.commands.MsgCommand;
import com.webforj.builtwithwebforj.terminal.commands.PromptCommand;
import com.webforj.builtwithwebforj.terminal.commands.Shell;
import com.webforj.builtwithwebforj.terminal.commands.SnakeCommand;
import com.webforj.builtwithwebforj.terminal.commands.TerminalCommand;
import com.webforj.builtwithwebforj.terminal.commands.TimeCommand;
import com.webforj.component.Composite;
import com.webforj.component.terminal.Terminal;
import com.webforj.component.terminal.event.TerminalDataEvent;
import com.webforj.router.annotation.Route;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Route("/terminal")
public class TerminalView extends Composite<Terminal> implements Shell {

  private static final String PROMPT = "$ ";
  private static final String ARROW_UP = "\u001b[A";
  private static final String ARROW_DOWN = "\u001b[B";

  private final Terminal self = getBoundComponent();
  private final StringBuilder commandBuffer = new StringBuilder();
  private final List<String> commandHistory = new ArrayList<>();
  private final Map<String, TerminalCommand> commands = new LinkedHashMap<>();
  private int historyIndex = 0;
  private InputHandler inputHandler;

  public TerminalView() {
    self.setAutoFit(true)
        .setSize("100%", "100vh")
        .onData(this::onData);

    self.writeln("\u001B[1;32m╔═══════════════════════════════════════════════════════╗\u001B[0m");
    self.writeln("\u001B[1;32m║                                                       ║\u001B[0m");
    self.writeln("\u001B[1;32m║        Welcome to the webforJ Terminal Demo!          ║\u001B[0m");
    self.writeln("\u001B[1;32m║                                                       ║\u001B[0m");
    self.writeln("\u001B[1;32m╚═══════════════════════════════════════════════════════╝\u001B[0m");
    self.writeln("");
    self.writeln("Type \u001B[1;33mhelp\u001B[0m to see available commands.");
    self.writeln("Type \u001B[1;33msnake\u001B[0m to play the Snake game!");
    self.writeln("Use \u001B[1;33m↑↓\u001B[0m arrow keys to navigate history.");
    self.writeln("");
    self.write(PROMPT);

    registerCommands();
    self.focus();
  }

  private void registerCommands() {
    List<TerminalCommand> commandList = List.of(
        new HelpCommand(commands),
        new ClearCommand(),
        new TimeCommand(),
        new SnakeCommand(),
        new MsgCommand(),
        new PromptCommand(),
        new HistoryCommand(commandHistory));

    for (TerminalCommand command : commandList) {
      commands.put(command.getName(), command);
    }
  }

  @Override
  public Terminal getTerminal() {
    return self;
  }

  @Override
  public void captureInput(InputHandler handler) {
    inputHandler = handler;
  }

  @Override
  public void releaseInput() {
    inputHandler = null;
    self.write(PROMPT);
  }

  @Override
  protected void onDidDestroy() {
    if (inputHandler != null) {
      inputHandler.close();
      inputHandler = null;
    }
  }

  private void onData(TerminalDataEvent e) {
    String input = e.getValue();

    if (inputHandler != null) {
      inputHandler.onInput(input);
      return;
    }

    if (input.startsWith("\u001b")) {
      navigateHistory(input);
      return;
    }

    // A paste arrives as one event, so handle it character by character.
    for (int codePoint : input.codePoints().toArray()) {
      if (inputHandler != null) {
        // A command started by the paste took over the input.
        return;
      }
      handleCharacter(codePoint);
    }
  }

  private void handleCharacter(int codePoint) {
    switch (codePoint) {
      case '\r' -> {
        self.write("\r\n");
        runCommandLine();
      }
      case '\n' -> {
        // Enter arrives as \r, a pasted \r\n must not run the line twice.
      }
      case 0x7F, '\b' -> {
        if (commandBuffer.length() > 0) {
          commandBuffer.setLength(commandBuffer.offsetByCodePoints(commandBuffer.length(), -1));
          self.write("\b \b");
        }
      }
      default -> {
        if ((codePoint >= 0x20 && codePoint <= 0x7E) || codePoint >= 0xA0) {
          commandBuffer.appendCodePoint(codePoint);
          self.write(Character.toString(codePoint));
        }
      }
    }
  }

  private void navigateHistory(String key) {
    if (ARROW_UP.equals(key) && historyIndex > 0) {
      historyIndex--;
      replaceCommandBuffer(commandHistory.get(historyIndex));
    } else if (ARROW_DOWN.equals(key) && historyIndex < commandHistory.size()) {
      historyIndex++;
      replaceCommandBuffer(historyIndex < commandHistory.size() ? commandHistory.get(historyIndex) : "");
    }
  }

  private void replaceCommandBuffer(String newBuffer) {
    self.write("\b \b".repeat(commandBuffer.codePointCount(0, commandBuffer.length())));
    commandBuffer.setLength(0);
    commandBuffer.append(newBuffer);
    self.write(newBuffer);
  }

  private void runCommandLine() {
    String commandLine = commandBuffer.toString();
    commandBuffer.setLength(0);

    if (!commandLine.isBlank()) {
      commandHistory.add(commandLine);
      runCommand(commandLine.trim().split("\\s+"));
    }
    historyIndex = commandHistory.size();

    // A command that captured the input shows the prompt itself when it releases it.
    if (inputHandler == null) {
      self.write(PROMPT);
    }
  }

  private void runCommand(String[] parts) {
    String commandName = parts[0].toLowerCase();
    TerminalCommand command = commands.get(commandName);
    if (command == null) {
      self.writeln("\u001B[1;31mCommand not found: " + commandName + "\u001B[0m");
      self.writeln("Type \u001B[1;33mhelp\u001B[0m to see available commands.");
      return;
    }

    try {
      command.execute(this, parts);
    } catch (Exception e) {
      self.writeln("\u001B[1;31mError executing command: " + e.getMessage() + "\u001B[0m");
    }
  }
}
