package com.webforj.builtwithwebforj.terminal.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.terminal.Application;
import com.webforj.component.terminal.Terminal;
import com.webforj.testbed.ShownOptionDialog;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(app = Application.class)
class TerminalViewTest {

  private static final String UP = "\u001b[A";
  private static final String DOWN = "\u001b[B";
  private static final String LEFT = "\u001b[D";

  private static Terminal openTerminal(WebforjTestbed context) {
    context.navigate(TerminalView.class);
    return ComponentLocator.of(Terminal.class).locate();
  }

  private static void run(Terminal terminal, String command) {
    UserInteraction.type(terminal, command);
    UserInteraction.type(terminal, "\r");
  }

  private static List<String> dialogMessages(WebforjTestbed context) {
    return context.getShownOptionDialogs().stream().map(ShownOptionDialog::getMessage).toList();
  }

  /** Runs the game timer until the game ends and returns how many steps the snake took. */
  private static int stepsUntilTheGameEnds(WebforjTestbed context) {
    int steps = 0;
    while (context.runIntervals() > 0) {
      steps++;
      if (steps > 100) {
        fail("The game did not end");
      }
    }
    return steps;
  }

  @Test
  void shouldShowTheMsgTextInAMessageDialog(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "msg Hello world");

    var dialog = context.getShownOptionDialogs().get(0);
    assertEquals("Terminal Message", dialog.getTitle());
    assertEquals("Hello world", dialog.getMessage());
  }

  @Test
  void shouldEraseTheLastCharacterOnBackspace(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    UserInteraction.type(terminal, "msg HelloX");
    UserInteraction.type(terminal, "\u007F");
    UserInteraction.type(terminal, "\r");

    assertEquals(List.of("Hello"), dialogMessages(context));
  }

  @Test
  void shouldAskThePromptQuestionInAnInputDialog(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    context.answerInputDialog("blue");
    run(terminal, "prompt Favorite color?");
    context.answerInputDialog("");
    run(terminal, "prompt Anything else?");

    var dialog = context.getShownOptionDialogs().get(0);
    assertEquals("Terminal Prompt", dialog.getTitle());
    assertEquals(List.of("Favorite color?", "Anything else?"), dialogMessages(context));
  }

  @Test
  void shouldShowNoDialogForAnUnknownCommand(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "nope");
    run(terminal, "   ");

    assertTrue(context.getShownOptionDialogs().isEmpty());
  }

  @Test
  void shouldRunAPastedLine(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    UserInteraction.type(terminal, "msg pasted\r\n");

    assertEquals(List.of("pasted"), dialogMessages(context));
  }

  @Test
  void shouldRunThePreviousCommandAgainFromTheHistory(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "msg First");
    run(terminal, "msg Second");
    UserInteraction.type(terminal, UP);
    UserInteraction.type(terminal, UP);
    UserInteraction.type(terminal, UP);
    UserInteraction.type(terminal, "\r");

    assertEquals(List.of("First", "Second", "First"), dialogMessages(context));
  }

  @Test
  void shouldEmptyTheLineWhenDownMovesPastTheNewestCommand(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "msg Hello");
    UserInteraction.type(terminal, UP);
    UserInteraction.type(terminal, DOWN);
    UserInteraction.type(terminal, DOWN);
    UserInteraction.type(terminal, "\r");

    assertEquals(List.of("Hello"), dialogMessages(context));
  }

  @Test
  void shouldKeepTheKeysPressedDuringTheGameOutOfTheNextCommand(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "snake");
    run(terminal, "msg during");
    UserInteraction.type(terminal, "q");
    run(terminal, "msg after");

    assertEquals(List.of("after"), dialogMessages(context));
    assertEquals(0, context.runIntervals());
  }

  @Test
  void shouldEndTheGameWhenTheSnakeHitsTheWall(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "snake");

    // The snake starts in the middle of a 40 wide board, moving right.
    assertEquals(20, stepsUntilTheGameEnds(context));
    run(terminal, "msg after");
    assertEquals(List.of("after"), dialogMessages(context));
  }

  @Test
  void shouldSteerTheSnakeWithTheArrowKeys(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "snake");
    UserInteraction.type(terminal, UP);

    // The snake starts on row 10 of a 20 high board.
    assertEquals(11, stepsUntilTheGameEnds(context));
  }

  @Test
  void shouldIgnoreATurnBackOntoTheSnake(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "snake");
    UserInteraction.type(terminal, LEFT);

    assertEquals(20, stepsUntilTheGameEnds(context));
  }

  @Test
  void shouldStopTheGameWhenTheUserLeavesTheTerminal(WebforjTestbed context) {
    Terminal terminal = openTerminal(context);

    run(terminal, "snake");
    context.navigate(HomeView.class);

    assertEquals(0, context.runIntervals());
  }
}
