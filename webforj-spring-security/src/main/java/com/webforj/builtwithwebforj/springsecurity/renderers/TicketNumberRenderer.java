package com.webforj.builtwithwebforj.springsecurity.renderers;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying ticket numbers with monospace styling.
 */
public class TicketNumberRenderer extends Renderer<Ticket> {

  @Override
  public String build() {
    return /* html */"""
        <style>
          .ticket-number {
            font-family: 'JetBrains Mono', 'Fira Code', 'SF Mono', monospace;
            font-size: 13px;
            color: hsl(0 0% 68%);
            background: hsl(0 0% 100% / 0.05);
            padding: 4px 10px;
            border-radius: 4px;
            letter-spacing: 0.02em;
          }
        </style>
        <span class="ticket-number"><%= cell.row.getValue('ticketNumber') %></span>
        """;
  }
}
