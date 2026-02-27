package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.component.Composite;
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
        createMetadataBox("Type", createTypeBadge(ticket)),
        createMetadataBox("Priority", createPriorityLabel(ticket)),
        createMetadataBox("Status", createStatusBadge(ticket)),
        createMetadataBox("Created By", new Span(ticket.getCreatedBy().getDisplayName())),
        createMetadataBox("Created", new Span(ticket.getCreatedAt().format(DATE_FORMATTER)))
    );
  }

  private Div createMetadataBox(String label, Span value) {
    Div box = new Div();
    box.addClassName("metadata-item");

    Span labelSpan = new Span(label);
    labelSpan.addClassName("metadata-label");

    value.addClassName("metadata-value");

    box.add(labelSpan, value);
    return box;
  }

  private Span createTypeBadge(Ticket ticket) {
    Span badge = new Span(ticket.getType().getDisplayName());
    badge.addClassName("type-badge");
    badge.addClassName(ticket.getType().name().toLowerCase().replace('_', '-'));
    return badge;
  }

  private Span createPriorityLabel(Ticket ticket) {
    Span label = new Span(ticket.getPriority().getDisplayName());
    label.addClassName("priority-label");
    label.addClassName(ticket.getPriority().name().toLowerCase().replace('_', '-'));
    return label;
  }

  private Span createStatusBadge(Ticket ticket) {
    Span badge = new Span(formatStatus(ticket.getStatus().name()));
    badge.addClassName("status-badge");
    badge.addClassName(ticket.getStatus().name().toLowerCase().replace('_', '-'));
    return badge;
  }

  private String formatStatus(String status) {
    return status.substring(0, 1) + status.substring(1).toLowerCase().replace('_', ' ');
  }
}
