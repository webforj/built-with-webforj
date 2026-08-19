package com.webforj.builtwithwebforj.springsecurity.entity.ticket;

/**
 * Types of support tickets.
 */
public enum TicketType {
  BUG("Bug"),
  FEATURE_REQUEST("Feature Request"),
  QUESTION("Question"),
  OTHER("Other");

  private final String displayName;

  TicketType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
