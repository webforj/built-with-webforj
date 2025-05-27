package com.webforj.tictactoe.views;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.toast.Toast;
import com.webforj.environment.namespace.PrivateNamespace;
import com.webforj.environment.namespace.exception.NamespaceLockedException;
import com.webforj.router.NavigationContext;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.concern.HasFrameTitle;
import com.webforj.router.event.WillEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.WillEnterObserver;
import com.webforj.tictactoe.components.Canvas;
import com.webforj.tictactoe.core.Game;
import com.webforj.tictactoe.core.GameResult;
import com.webforj.tictactoe.core.Player;

@Route("/game/:key/:nickname/:action")
public class GameView extends Composite<Div> implements WillEnterObserver, HasFrameTitle {
  private static final String ENGINE_KEY = "engine";
  private static final String ACTION_START = "start";
  private static final String ACTION_JOIN = "join";
  private static final String ACTION_HOME = "home";
  private final Div self = getBoundComponent();
  private Game game;

  @Override
  public String getFrameTitle(NavigationContext context, ParametersBag parameters) {
    String action = parameters.get("action").orElse("");
    if (action.isBlank()) {
      return "";
    }

    return String.format(
        "[%s] Running Game",
        ACTION_START.equals(action) ? "Host" : "Guest");
  }

  @Override
  public void onWillEnter(WillEnterEvent event, ParametersBag parameters) {
    String code = parameters.get("key").orElse("");
    String action = parameters.get("action").orElse(ACTION_HOME);
    boolean veto = code.isEmpty() || !PrivateNamespace.isPresent(code);
    event.veto(veto);

    if (veto) {
      Class<? extends Component> view = HomeView.class;

      if (ACTION_JOIN.equals(action)) {
        view = JoinView.class;
      } else if (ACTION_START.equals(action)) {
        view = StartView.class;
      }

      Router.getCurrent().navigate(view);
      Toast.show(
          "Invalid game key or the game has not been started yet.",
          Theme.DANGER);
    } else {
      PrivateNamespace namespace = PrivateNamespace.ofExisting(code);
      String nickname = parameters.getAlpha("nickname").orElse("");
      if (ACTION_START.equals(action)) {
        startGame(namespace, nickname.isBlank() ? "X" : nickname);
      } else {
        joinGame(namespace, nickname.isBlank() ? "O" : nickname);
      }
    }
  }

  private void startGame(PrivateNamespace namespace, String nickname) {
    Object obj = namespace.getOrDefault(ENGINE_KEY, null);
    if (obj instanceof Game theGame) {
      game = theGame;
      game.update();
    }

    if (game == null || game.getState().getResult() != GameResult.CONTINUE) {
      game = new Game(nickname, "O");
      game.update();
    }

    try {
      namespace.atomicPut(ENGINE_KEY, game);
      render(game.getPlayerX());
    } catch (NamespaceLockedException e) {
      Router.getCurrent().navigate(HomeView.class);
      Toast.show(
          "Failed to set up the game. Please try again.",
          Theme.DANGER);
    }
  }

  private void joinGame(PrivateNamespace namespace, String nickname) {
    Object obj = namespace.getOrDefault(ENGINE_KEY, null);
    if (obj instanceof Game gameEngine) {
      game = gameEngine;
      game.getPlayerO().setName(nickname);
      game.update();
      render(game.getPlayerO());

      if (game.getState().getResult() != GameResult.CONTINUE) {
        Toast.show(
            "The game has ended. Please start a new game.",
            Theme.INFO);
      }

    } else {
      Router.getCurrent().navigate(HomeView.class);
      Toast.show(
          "Failed to join the game. Please try again.",
          Theme.DANGER);
    }
  }

  private void render(Player owner) {
    self.add(new Canvas(game, owner));
  }
}
