package com.webforj.builtwithwebforj.springsecurity.entity.ticket;

/**
 * Priority levels for support tickets.
 */
public enum TicketPriority {
  LOW("Low", "#6B7280"),
  MEDIUM("Medium", "#3B82F6"),
  HIGH("High", "#F59E0B"),
  URGENT("Urgent", "#EF4444");

  private final String displayName;
  private final String color;

  TicketPriority(String displayName, String color) {
    this.displayName = displayName;
    this.color = color;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getColor() {
    return color;
  }
}
