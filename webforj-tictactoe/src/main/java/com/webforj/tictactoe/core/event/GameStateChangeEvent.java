package com.webforj.tictactoe.core.event;

import java.util.EventObject;

import com.webforj.tictactoe.core.Game;

/**
 * Event fired when the game state changes.
 */
public class GameStateChangeEvent extends EventObject {

  /**
   * Constructs a new GameStateChangedEvent.
   *
   * @param source the source of the event
   */
  public GameStateChangeEvent(Game source) {
    super(source);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Game getSource() {
    return (Game) super.getSource();
  }
}
