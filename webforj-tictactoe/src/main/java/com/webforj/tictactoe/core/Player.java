package com.webforj.tictactoe.core;

/**
 * Represents a player in the game.
 */
public class Player {
  private final String uuid = java.util.UUID.randomUUID().toString();
  private final String symbol;
  private String name;

  /**
   * Constructs a new Player with the specified name and symbol.
   *
   * @param name   the name of the player
   * @param symbol the symbol of the player (e.g., "X" or "O")
   */
  public Player(String name, String symbol) {
    this.name = name;
    this.symbol = symbol;
  }

  /**
   * Returns the name of the player.
   *
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the player.
   *
   * @param name the new name of the player
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the symbol of the player.
   *
   * @return the symbol of the player
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
    result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Player other = (Player) obj;
    if (uuid == null) {
      if (other.uuid != null)
        return false;
    } else if (!uuid.equals(other.uuid))
      return false;
    if (symbol == null) {
      if (other.symbol != null)
        return false;
    } else if (!symbol.equals(other.symbol))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
