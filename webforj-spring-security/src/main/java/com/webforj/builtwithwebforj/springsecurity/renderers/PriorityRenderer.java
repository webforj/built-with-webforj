package com.webforj.builtwithwebforj.springsecurity.renderers;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying ticket priority with color.
 */
public class PriorityRenderer extends Renderer<Ticket> {

  @Override
  public String build() {
    return /* html */"""
        <%
        const priorityName = cell.row.getValue('priorityDisplay');
        const priorityColor = cell.row.getValue('priorityColor');
        %>
        <span part='priority-text' style='color: <%= priorityColor %>; font-weight: 600;'>
          <%= priorityName %>
        </span>
        """;
  }
}
