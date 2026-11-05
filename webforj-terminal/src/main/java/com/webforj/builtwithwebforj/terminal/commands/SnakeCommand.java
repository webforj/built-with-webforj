package com.webforj.builtwithwebforj.terminal.commands;

import com.webforj.Interval;
import com.webforj.builtwithwebforj.terminal.commands.SnakeBoard.Direction;
import com.webforj.builtwithwebforj.terminal.commands.SnakeBoard.Point;
import com.webforj.component.terminal.Terminal;
import java.util.Random;

public class SnakeCommand implements TerminalCommand {

  private static final int WIDTH = 40;
  private static final int HEIGHT = 20;
  private static final float TICK_SECONDS = 0.15f;

  private final Random random;

  public SnakeCommand() {
    this(new Random());
  }

  SnakeCommand(Random random) {
    this.random = random;
  }

  @Override
  public String getName() {
    return "snake";
  }

  @Override
  public String getDescription() {
    return "Play the classic Snake game (Use arrow keys to move, 'q' to quit)";
  }

  @Override
  public void execute(Shell shell, String[] args) {
    new Game(shell, new SnakeBoard(WIDTH, HEIGHT, random)).start();
  }

  private static final class Game implements InputHandler {
    private final Shell shell;
    private final Terminal terminal;
    private final SnakeBoard board;
    private Interval interval;

    Game(Shell shell, SnakeBoard board) {
      this.shell = shell;
      this.terminal = shell.getTerminal();
      this.board = board;
    }

    void start() {
      shell.captureInput(this);
      terminal.clear();
      drawWelcome();
      draw();
      interval = new Interval(TICK_SECONDS, e -> tick());
      interval.start();
    }

    @Override
    public void onInput(String data) {
      switch (data) {
        case "\u001b[A" -> board.turn(Direction.UP);
        case "\u001b[B" -> board.turn(Direction.DOWN);
        case "\u001b[C" -> board.turn(Direction.RIGHT);
        case "\u001b[D" -> board.turn(Direction.LEFT);
        case "q", "Q" -> quit();
        default -> {
          // Every other key is ignored while the game runs.
        }
      }
    }

    @Override
    public void close() {
      interval.stop();
    }

    private void tick() {
      switch (board.step()) {
        case HIT_WALL -> gameOver("You hit the wall!");
        case BIT_SELF -> gameOver("You bit yourself!");
        default -> draw();
      }
    }

    private void drawWelcome() {
      terminal.writeln("\u001B[1;36m╔════════════════════════════════════════════════════════════════╗\u001B[0m");
      terminal.writeln("\u001B[1;36m║                         SNAKE GAME                             ║\u001B[0m");
      terminal.writeln("\u001B[1;36m╚════════════════════════════════════════════════════════════════╝\u001B[0m");
      terminal.writeln("\u001B[1;33mUse Arrow Keys to move  |  Press 'Q' to quit\u001B[0m");
      terminal.writeln("");
    }

    private void draw() {
      StringBuilder frame = new StringBuilder();

      // Move the cursor to the game area
      frame.append("\u001B[6;0H");

      frame.append("\u001B[1;34m┌").append("─".repeat(board.width())).append("┐\u001B[0m\n");
      for (int y = 0; y < board.height(); y++) {
        frame.append("\u001B[1;34m│\u001B[0m");
        for (int x = 0; x < board.width(); x++) {
          Point p = new Point(x, y);
          if (p.equals(board.head())) {
            frame.append("\u001B[1;32m●\u001B[0m");
          } else if (board.isSnake(p)) {
            frame.append("\u001B[32m○\u001B[0m");
          } else if (p.equals(board.food())) {
            frame.append("\u001B[1;31m★\u001B[0m");
          } else {
            frame.append(" ");
          }
        }
        frame.append("\u001B[1;34m│\u001B[0m\n");
      }
      frame.append("\u001B[1;34m└").append("─".repeat(board.width())).append("┘\u001B[0m\n\n");

      frame.append("\u001B[1;33mScore: ").append(board.score())
          .append("  |  Length: ").append(board.length())
          .append("\u001B[0m\n");

      terminal.write(frame.toString());
    }

    private void gameOver(String reason) {
      close();
      terminal.writeln("");
      terminal.writeln("\u001B[1;31m╔════════════════════════════════════════════════════════════════╗\u001B[0m");
      terminal.writeln("\u001B[1;31m║                          GAME OVER!                            ║\u001B[0m");
      terminal.writeln("\u001B[1;31m╚════════════════════════════════════════════════════════════════╝\u001B[0m");
      terminal.writeln("");
      terminal.writeln("\u001B[1;33m" + reason + "\u001B[0m");
      terminal.writeln("\u001B[1;36mFinal Score: " + board.score() + "\u001B[0m");
      terminal.writeln("\u001B[1;36mFinal Length: " + board.length() + "\u001B[0m");
      terminal.writeln("");
      terminal.writeln("Type \u001B[1;32msnake\u001B[0m to play again!");
      terminal.writeln("");
      shell.releaseInput();
    }

    private void quit() {
      close();
      terminal.writeln("");
      terminal.writeln("\u001B[1;33mGame stopped. Final score: " + board.score() + "\u001B[0m");
      terminal.writeln("");
      shell.releaseInput();
    }
  }
}
