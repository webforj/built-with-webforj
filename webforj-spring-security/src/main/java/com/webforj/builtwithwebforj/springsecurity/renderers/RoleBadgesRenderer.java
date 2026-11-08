package com.webforj.builtwithwebforj.springsecurity.renderers;

import com.webforj.component.table.renderer.Renderer;

/**
 * Renders comma-separated roles as individual colored badge pills.
 */
public class RoleBadgesRenderer<T> extends Renderer<T> {

  @Override
  public String build() {
    return /* html */ """
      <%
        var roles = (cell.value || '').split(',').map(function(r) { return r.trim(); }).filter(Boolean);
        var colors = {
          'USER': 'success',
          'SUPPORT': 'warning',
          'ADMIN': 'primary'
        };
      %>
      <div style="display:flex;gap:4px;flex-wrap:wrap;align-items:center;">
        <% roles.forEach(function(role) {
          var theme = colors[role] || 'default';
        %>
          <dwc-badge theme="<%= theme %>" expanse="xs"><%= role %></dwc-badge>
        <% }); %>
      </div>
    """;
  }
}
