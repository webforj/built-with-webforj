package com.webforj.builtwithwebforj.springsecurity.renderers.user;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying email addresses as clickable mailto: links.
 */
public class EmailLinkRenderer extends Renderer<User> {

  @Override
  public String build() {
    return /* html */"""
        <style>
          .email-link {
            color: var(--dwc-color-primary-text, hsl(230 85% 65%));
            text-decoration: none;
            transition: color 150ms ease;
          }
          .email-link:hover {
            color: var(--dwc-color-primary-text-light, hsl(230 85% 75%));
            text-decoration: underline;
          }
          .email-empty {
            color: var(--dwc-color-default-50, hsl(0 0% 50%));
            font-style: italic;
          }
        </style>
        <%
        const email = cell.row.getValue('email');
        %>
        <% if (email && email.trim() !== '') { %>
          <a class="email-link" href="mailto:<%= email %>" onclick="event.stopPropagation();">
            <%= email %>
          </a>
        <% } else { %>
          <span class="email-empty">No email</span>
        <% } %>
        """;
  }
}
