package com.webforj.builtwithwebforj.springsecurity.renderers.user;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying user roles as colored chips/badges.
 */
public class RolesChipRenderer extends Renderer<User> {

  @Override
  public String build() {
    return /* html */"""
        <style>
          .roles-container {
            display: flex;
            flex-wrap: wrap;
            gap: 6px;
          }
          .role-chip {
            display: inline-flex;
            align-items: center;
            padding: 3px 10px;
            border-radius: 100px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.04em;
            border: 1px solid transparent;
          }
          .role-chip.admin {
            background: hsl(280 70% 50% / 0.15);
            border-color: hsl(280 70% 50% / 0.3);
            color: hsl(280 70% 70%);
          }
          .role-chip.support {
            background: hsl(200 85% 50% / 0.15);
            border-color: hsl(200 85% 50% / 0.3);
            color: hsl(200 85% 65%);
          }
          .role-chip.user {
            background: hsl(160 60% 40% / 0.15);
            border-color: hsl(160 60% 40% / 0.3);
            color: hsl(160 60% 55%);
          }
        </style>
        <%
        const rolesStr = cell.row.getValue('roles');
        const roles = rolesStr ? rolesStr.split(', ').filter(r => r.trim()) : [];
        %>
        <div class="roles-container">
          <% roles.forEach(role => { %>
            <% const roleKey = role.toLowerCase().replace('role_', ''); %>
            <span class="role-chip <%= roleKey %>"><%= roleKey %></span>
          <% }); %>
        </div>
        """;
  }
}
