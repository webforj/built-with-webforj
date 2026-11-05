package com.webforj.builtwithwebforj.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webforj.component.terminal.Terminal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CommandsTest {

  private Terminal terminal;
  private Shell shell;

  @BeforeEach
  void setUp() {
    terminal = mock(Terminal.class);
    shell = mock(Shell.class);
    when(shell.getTerminal()).thenReturn(terminal);
  }

  private List<String> written() {
    ArgumentCaptor<Object> lines = ArgumentCaptor.forClass(Object.class);
    verify(terminal, atLeastOnce()).writeln(lines.capture());
    return lines.getAllValues().stream().map(String::valueOf).toList();
  }

  @Test
  void shouldListTheCommandsInTheOrderTheyWereRegistered() {
    Map<String, TerminalCommand> commands = new LinkedHashMap<>();
    for (TerminalCommand c : List.of(new HelpCommand(commands), new TimeCommand(),
        new ClearCommand(), new SnakeCommand())) {
      commands.put(c.getName(), c);
    }

    commands.get("help").execute(shell, new String[] {"help"});

    List<String> names = written().stream().filter(l -> l.contains(" - "))
        .map(l -> l.replaceAll("\u001B\\[[0-9;]*m", "").trim().split("\\s+")[0]).toList();
    assertEquals(List.of("help", "time", "clear", "snake"), names);
  }

  @Test
  void shouldClearTheScreen() {
    new ClearCommand().execute(shell, new String[] {"clear"});

    verify(terminal).clear();
  }

  @Test
  void shouldShowTheCurrentTime() {
    new TimeCommand().execute(shell, new String[] {"time"});

    assertTrue(written().get(0).matches(".*Current time: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*"));
  }

  @Test
  void shouldSayWhenThereIsNoHistory() {
    new HistoryCommand(new ArrayList<>()).execute(shell, new String[] {"history"});

    assertTrue(written().get(0).contains("No command history yet"));
  }

  @Test
  void shouldListTheHistoryNumbered() {
    new HistoryCommand(List.of("help", "time")).execute(shell, new String[] {"history"});

    List<String> lines = written();
    assertTrue(lines.get(1).contains("1") && lines.get(1).endsWith("help"));
    assertTrue(lines.get(2).contains("2") && lines.get(2).endsWith("time"));
  }

  @Test
  void shouldShowTheUsageWhenMsgHasNoText() {
    new MsgCommand().execute(shell, new String[] {"msg"});

    assertTrue(written().get(0).contains("Usage: msg <message>"));
  }

  @Test
  void shouldShowTheUsageWhenPromptHasNoQuestion() {
    new PromptCommand().execute(shell, new String[] {"prompt"});

    assertTrue(written().get(0).contains("Usage: prompt <question>"));
  }
}
