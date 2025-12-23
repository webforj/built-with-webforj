package com.webforj.builtwithwebforj.springsecurity.renderers.ticket;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying ticket type as a colored badge.
 */
public class TypeBadgeRenderer extends Renderer<Ticket> {

  @Override
  public String build() {
    return /* html */"""
        <style>
          .type-badge {
            display: inline-flex;
            align-items: center;
            width: fit-content;
            padding: 4px 12px;
            border-radius: 100px;
            font-size: 13px;
            font-weight: 500;
            border: 1px solid transparent;
          }
          .type-badge.bug {
            background: hsl(0 72% 51% / 0.12);
            border-color: hsl(0 72% 51% / 0.25);
            color: hsl(0 72% 68%);
          }
          .type-badge.feature-request {
            background: hsl(160 84% 39% / 0.12);
            border-color: hsl(160 84% 39% / 0.25);
            color: hsl(160 84% 55%);
          }
          .type-badge.question {
            background: hsl(230 85% 60% / 0.12);
            border-color: hsl(230 85% 60% / 0.25);
            color: hsl(230 85% 70%);
          }
          .type-badge.other {
            background: hsl(0 0% 50% / 0.1);
            border-color: hsl(0 0% 50% / 0.2);
            color: hsl(0 0% 60%);
          }
        </style>
        <%
        const typeName = cell.row.getValue('typeDisplay');
        const typeKey = cell.row.getValue('type');
        const cssClass = typeKey ? typeKey.toLowerCase().replace(/_/g, '-') : 'other';
        %>
        <span class="type-badge <%= cssClass %>">
          <%= typeName %>
        </span>
        """;
  }
}
