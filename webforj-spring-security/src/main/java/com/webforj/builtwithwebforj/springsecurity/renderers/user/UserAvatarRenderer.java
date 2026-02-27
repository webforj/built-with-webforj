package com.webforj.builtwithwebforj.springsecurity.renderers.user;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.component.table.renderer.Renderer;

/**
 * Renderer for displaying user avatars using the dwc-avatar web component.
 */
public class UserAvatarRenderer extends Renderer<User> {

  @Override
  public String build() {
    return /* html */"""
        <style>
          .user-avatar-cell {
            display: flex;
            align-items: center;
            gap: 10px;
          }
          .user-avatar-cell dwc-avatar {
            --dwc-avatar-size: 32px;
          }
          .user-avatar-name {
            font-weight: 500;
          }
        </style>
        <%
        const username = cell.row.getValue('username') || '';
        const displayName = cell.row.getValue('displayName') || username;
        %>
        <div class="user-avatar-cell">
          <dwc-avatar name="<%= displayName %>"></dwc-avatar>
          <span class="user-avatar-name"><%= username %></span>
        </div>
        """;
  }
}
