package com.webforj.builtwithwebforj.terminal.commands;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The rules of the Snake game, without any drawing.
 */
final class SnakeBoard {

  enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    final int dx;
    final int dy;

    Direction(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }

    boolean isOpposite(Direction other) {
      return dx == -other.dx && dy == -other.dy;
    }
  }

  enum Step {
    MOVED, ATE, HIT_WALL, BIT_SELF
  }

  record Point(int x, int y) {}

  private final int width;
  private final int height;
  private final Random random;
  private final Deque<Point> snake = new ArrayDeque<>();
  private final Set<Point> cells = new HashSet<>();
  private Direction direction;
  private Direction nextDirection;
  private Point food;
  private int score;

  SnakeBoard(int width, int height, Random random) {
    this(width, height, random, List.of(new Point(width / 2, height / 2),
        new Point(width / 2 - 1, height / 2), new Point(width / 2 - 2, height / 2)),
        Direction.RIGHT, null);
  }

  /**
   * Creates a board with the given snake, head first, and the given food, or random food when it
   * is null.
   */
  SnakeBoard(int width, int height, Random random, List<Point> body, Direction direction,
      Point food) {
    this.width = width;
    this.height = height;
    this.random = random;
    this.direction = direction;
    this.nextDirection = direction;
    for (Point p : body) {
      snake.addLast(p);
      cells.add(p);
    }
    if (food != null) {
      this.food = food;
    } else {
      placeFood();
    }
  }

  /**
   * Turns the snake on the next step. Turning back onto itself is ignored.
   */
  void turn(Direction requested) {
    if (!requested.isOpposite(direction)) {
      nextDirection = requested;
    }
  }

  Step step() {
    direction = nextDirection;
    Point head = snake.peekFirst();
    Point next = new Point(head.x() + direction.dx, head.y() + direction.dy);

    if (next.x() < 0 || next.x() >= width || next.y() < 0 || next.y() >= height) {
      return Step.HIT_WALL;
    }

    boolean eats = next.equals(food);
    Point tail = snake.peekLast();
    // The tail moves away on this step unless the snake grows, so its cell is free to enter.
    if (cells.contains(next) && (eats || !next.equals(tail))) {
      return Step.BIT_SELF;
    }

    if (!eats) {
      snake.removeLast();
      cells.remove(tail);
    }
    snake.addFirst(next);
    cells.add(next);

    if (eats) {
      score += 10;
      placeFood();
      return Step.ATE;
    }
    return Step.MOVED;
  }

  private void placeFood() {
    if (cells.size() >= width * height) {
      food = null;
      return;
    }
    do {
      food = new Point(random.nextInt(width), random.nextInt(height));
    } while (cells.contains(food));
  }

  int width() {
    return width;
  }

  int height() {
    return height;
  }

  Point head() {
    return snake.peekFirst();
  }

  boolean isSnake(Point p) {
    return cells.contains(p);
  }

  Point food() {
    return food;
  }

  int score() {
    return score;
  }

  int length() {
    return snake.size();
  }
}
