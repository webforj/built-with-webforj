package com.webforj.builtwithwebforj.springsecurity.entity.ticket;

/**
 * Types of support tickets.
 */
public enum TicketType {
  BUG("Bug", "B", "#EF4444"),
  FEATURE_REQUEST("Feature Request", "F", "#10B981"),
  QUESTION("Question", "Q", "#3B82F6"),
  OTHER("Other", "O", "#6B7280");

  private final String displayName;
  private final String badgeLetter;
  private final String color;

  TicketType(String displayName, String badgeLetter, String color) {
    this.displayName = displayName;
    this.badgeLetter = badgeLetter;
    this.color = color;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getBadgeLetter() {
    return badgeLetter;
  }

  public String getColor() {
    return color;
  }
}
