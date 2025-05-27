package com.webforj.tictactoe.components;

import java.util.ArrayList;
import java.util.List;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.element.Element;
import com.webforj.component.element.event.ElementEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.concern.HasClassName;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.tictactoe.core.Game;
import com.webforj.tictactoe.core.GameResult;
import com.webforj.tictactoe.core.GameState;
import com.webforj.tictactoe.core.Player;
import com.webforj.tictactoe.core.SoundManager;
import com.webforj.tictactoe.core.event.GameStateChangeEvent;

public class Canvas extends Composite<Div> implements HasClassName<Canvas> {
  private final Game game;
  private final Player owner;
  private final Div self = getBoundComponent();
  private final Div status = new Div();
  private final Div board = new Div();
  private final List<Element> cells = new ArrayList<>();
  private final ListenerRegistration<GameStateChangeEvent> gameStateListener;
  private final Button startNewGame = new Button("Start New Game");

  public Canvas(Game game, Player owner) {
    this.owner = owner;
    this.game = game;

    gameStateListener = game.addGameStateChangedListener(event -> setState());

    self.addClassName("canvas");
    status.addClassName("status");
    board.addClassName("board");

    startNewGame.setVisible(false);
    startNewGame.addClassName("start-new-game");
    startNewGame.onClick(this::handleReset);

    self.add(
        new H1("Tic Tac Toe"),
        status,
        board,
        startNewGame);

    setBoard();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    gameStateListener.remove();
  }

  private void setBoard() {
    cells.forEach(Element::destroy);
    cells.clear();

    for (int i = 0; i < 9; i++) {
      Element cell = new Element("button");
      cell.addClassName("cell");
      cell.setUserData("index", i);
      cell.addEventListener("click", this::handleMove);
      cells.add(cell);
      board.add(cell);
    }

    setState();
  }

  private void setState() {
    GameState state = game.getState();
    String[] boardState = state.getBoard();

    self.removeClassName("win");
    self.removeClassName("lose");
    for (int i = 0; i < 9; i++) {
      Element cell = cells.get(i);
      cell.setText(boardState[i]);
      cell.removeClassName("winning");
      setCellState(cell, boardState[i]);
    }

    setResult(state);
  }

  private void setResult(GameState state) {
    GameResult result = state.getResult();
    boolean isDone = result != GameResult.CONTINUE;

    if (result == GameResult.WIN) {
      Player winner = game.getCurrentPlayer();
      if (winner.equals(owner)) {
        status.setText("You Win!");
        setHighlightWinnerCells();
        self.addClassName("win");
        SoundManager.getCurrent().playWin();
      } else {
        status.setText("You Lose!");
        self.addClassName("lose");
        SoundManager.getCurrent().playLose();
      }
    } else if (result == GameResult.DRAW) {
      status.setText("It's a Draw!");
      SoundManager.getCurrent().playDraw();
    } else {
      if (isOwnerTurn()) {
        status.setHtml(String.format("Your turn, <b>%s</b>", owner.getName()));
      } else {
        Player current = game.getCurrentPlayer();
        status.setHtml(String.format("Waiting for <b>%s</b> to make the next move", current.getName()));
      }
    }

    startNewGame.setVisible(isDone);
  }

  private void setCellState(Element cell, String cellValue) {
    if (!cellValue.isEmpty()) {
      if (cellValue.equals(owner.getSymbol())) {
        cell.addClassName("owner-move");
      } else {
        cell.removeClassName("owner-move");
      }
      cell.setAttribute("disabled", "true");
    } else {
      cell.removeAttribute("disabled");
      if (!isOwnerTurn()) {
        cell.setAttribute("disabled", "true");
      }
    }
  }

  private void setHighlightWinnerCells() {
    GameState state = game.getState();
    int[] winnerPattern = state.getWinningPattern();
    if (winnerPattern.length > 0) {
      for (int index : winnerPattern) {
        cells.get(index).addClassName("winning");
      }
    }
  }

  private boolean isOwnerTurn() {
    return owner.equals(game.getCurrentPlayer());
  }

  private boolean canMakeMove() {
    return game.getState().getResult() == GameResult.CONTINUE && isOwnerTurn();
  }

  private void handleMove(ElementEvent ev) {
    if (!canMakeMove()) {
      return;
    }

    int index = (int) ev.getComponent().getUserData("index");
    game.makeMove(index);
    SoundManager.getCurrent().playTap();

    setState();
  }

  private void handleReset(ButtonClickEvent ev) {
    game.reset();
    setState();
    game.update();
  }
}
