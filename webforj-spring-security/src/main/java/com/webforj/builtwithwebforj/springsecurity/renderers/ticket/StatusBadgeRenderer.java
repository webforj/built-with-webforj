package com.webforj.builtwithwebforj.springsecurity.renderers.ticket;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying ticket status as a colored badge.
 * Uses CSS variables defined in app.css for consistent styling.
 */
public class StatusBadgeRenderer extends Renderer<Ticket> {

  @Override
  public String build() {
    return /* html */"""
        <style>
          @keyframes status-pulse {
            0%, 100% { opacity: 1; transform: scale(1); }
            50% { opacity: 0.5; transform: scale(0.85); }
          }
          .status-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 2px 10px;
            border-radius: 100px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.04em;
            width: fit-content;
          }
          .status-dot {
            width: 6px;
            height: 6px;
            border-radius: 50%;
            background: currentColor;
            flex-shrink: 0;
          }
          .status-dot.pulse {
            animation: status-pulse 1.5s ease-in-out infinite;
          }
          .status-badge.open {
            background: hsl(230 85% 60% / 0.15);
            color: hsl(230 85% 70%);
          }
          .status-badge.in-progress {
            background: hsl(38 92% 50% / 0.15);
            color: hsl(38 92% 65%);
          }
          .status-badge.resolved {
            background: hsl(160 84% 39% / 0.15);
            color: hsl(160 84% 55%);
          }
          .status-badge.closed {
            background: hsl(0 0% 50% / 0.12);
            color: hsl(0 0% 55%);
          }
        </style>
        <%
        var status = cell.row.getValue('status').name();
        var cssClass = '';
        var displayText = '';
        var showPulse = false;

        switch(status) {
          case 'OPEN':
            cssClass = 'open';
            displayText = 'Open';
            showPulse = true;
            break;
          case 'IN_PROGRESS':
            cssClass = 'in-progress';
            displayText = 'In Progress';
            showPulse = true;
            break;
          case 'RESOLVED':
            cssClass = 'resolved';
            displayText = 'Resolved';
            break;
          case 'CLOSED':
            cssClass = 'closed';
            displayText = 'Closed';
            break;
        }
        %>
        <span class="status-badge <%= cssClass %>">
          <span class="status-dot<%= showPulse ? ' pulse' : '' %>"></span>
          <%= displayText %>
        </span>
        """;
  }
}
