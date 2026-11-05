package com.webforj.builtwithwebforj.terminal.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.webforj.builtwithwebforj.terminal.commands.SnakeBoard.Direction;
import com.webforj.builtwithwebforj.terminal.commands.SnakeBoard.Point;
import com.webforj.builtwithwebforj.terminal.commands.SnakeBoard.Step;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SnakeBoardTest {

  private static final Point FAR_FOOD = new Point(9, 9);

  private static SnakeBoard board(List<Point> body, Direction direction, Point food) {
    return new SnakeBoard(10, 10, new Random(1), body, direction, food);
  }

  @Test
  void shouldMoveOneCellPerStepAndKeepItsLength() {
    SnakeBoard board = board(List.of(new Point(2, 2), new Point(1, 2)), Direction.RIGHT, FAR_FOOD);

    assertEquals(Step.MOVED, board.step());
    assertEquals(new Point(3, 2), board.head());
    assertEquals(2, board.length());
    assertFalse(board.isSnake(new Point(1, 2)));
  }

  @Test
  void shouldGrowAndScoreWhenItEats() {
    SnakeBoard board =
        board(List.of(new Point(2, 2), new Point(1, 2)), Direction.RIGHT, new Point(3, 2));

    assertEquals(Step.ATE, board.step());
    assertEquals(3, board.length());
    assertEquals(10, board.score());
    assertNotNull(board.food());
    assertFalse(board.isSnake(board.food()));
  }

  @Test
  void shouldHitTheWall() {
    SnakeBoard board = board(List.of(new Point(9, 0)), Direction.RIGHT, FAR_FOOD);

    assertEquals(Step.HIT_WALL, board.step());
  }

  @Test
  void shouldBiteItself() {
    // A U shape whose head turns into its own body, not into the tail.
    SnakeBoard board = board(List.of(new Point(2, 2), new Point(2, 3), new Point(3, 3),
        new Point(3, 2), new Point(3, 1)), Direction.UP, FAR_FOOD);
    board.turn(Direction.RIGHT);

    assertEquals(Step.BIT_SELF, board.step());
  }

  @Test
  void shouldMoveIntoTheCellTheTailLeaves() {
    // A square of four cells, the head moves onto the tail as the tail moves away.
    SnakeBoard board = board(List.of(new Point(2, 2), new Point(2, 3), new Point(3, 3),
        new Point(3, 2)), Direction.UP, FAR_FOOD);
    board.turn(Direction.RIGHT);

    assertEquals(Step.MOVED, board.step());
    assertEquals(new Point(3, 2), board.head());
  }

  @Test
  void shouldIgnoreATurnBackOntoItself() {
    SnakeBoard board = board(List.of(new Point(2, 2), new Point(1, 2)), Direction.RIGHT, FAR_FOOD);
    board.turn(Direction.LEFT);

    assertEquals(Step.MOVED, board.step());
    assertEquals(new Point(3, 2), board.head());
  }

  @Test
  void shouldPlaceNoFoodWhenTheSnakeFillsTheBoard() {
    SnakeBoard board =
        new SnakeBoard(2, 1, new Random(1), List.of(new Point(0, 0)), Direction.RIGHT,
            new Point(1, 0));

    assertEquals(Step.ATE, board.step());
    assertNull(board.food());
  }

  @Test
  void shouldStartInTheMiddleWithFoodOffTheSnake() {
    SnakeBoard board = new SnakeBoard(40, 20, new Random(7));

    assertEquals(new Point(20, 10), board.head());
    assertEquals(3, board.length());
    assertFalse(board.isSnake(board.food()));
  }
}
