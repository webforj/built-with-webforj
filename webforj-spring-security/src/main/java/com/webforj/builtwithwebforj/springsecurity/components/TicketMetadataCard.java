package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketPriority;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.badge.Badge;
import com.webforj.component.badge.BadgeTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;

import java.time.format.DateTimeFormatter;

/**
 * Component for displaying ticket metadata (type, priority, status, created by, created date).
 */
public class TicketMetadataCard extends Composite<Div> {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

  public TicketMetadataCard(Ticket ticket) {
    Div container = getBoundComponent();
    container.addClassName("ticket-metadata");

    container.add(
        createMetadataBox("Type", new Badge(ticket.getType().getDisplayName(), BadgeTheme.OUTLINED_PRIMARY)),
        createMetadataBox("Priority", new Badge(ticket.getPriority().getDisplayName(), priorityTheme(ticket.getPriority()))),
        createMetadataBox("Status", new Badge(formatStatus(ticket.getStatus()), statusTheme(ticket.getStatus()))),
        createMetadataBox("Created By", new Span(ticket.getCreatedBy().getDisplayName())),
        createMetadataBox("Created", new Span(ticket.getCreatedAt().format(DATE_FORMATTER)))
    );
  }

  private Div createMetadataBox(String label, Component value) {
    Div box = new Div();
    box.addClassName("metadata-item");

    Span labelSpan = new Span(label);
    labelSpan.addClassName("metadata-label");

    if (value instanceof Span s) {
      s.addClassName("metadata-value");
    }

    box.add(labelSpan, value);
    return box;
  }

  private BadgeTheme priorityTheme(TicketPriority priority) {
    return switch (priority) {
      case LOW -> BadgeTheme.DEFAULT;
      case MEDIUM -> BadgeTheme.PRIMARY;
      case HIGH -> BadgeTheme.WARNING;
      case URGENT -> BadgeTheme.DANGER;
    };
  }

  private BadgeTheme statusTheme(TicketStatus status) {
    return switch (status) {
      case OPEN -> BadgeTheme.PRIMARY;
      case IN_PROGRESS -> BadgeTheme.WARNING;
      case RESOLVED -> BadgeTheme.SUCCESS;
      case CLOSED -> BadgeTheme.DEFAULT;
    };
  }

  private String formatStatus(TicketStatus status) {
    String name = status.name();
    return name.charAt(0) + name.substring(1).toLowerCase().replace('_', ' ');
  }
}
