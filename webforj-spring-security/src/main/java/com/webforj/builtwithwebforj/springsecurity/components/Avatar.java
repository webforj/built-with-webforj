package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Img;

@StyleSheet("ws://avatar.css")
public class Avatar extends Composite<Div> {

  private final Div container = getBoundComponent();
  private String imageUrl;
  private String name;
  private String role;

  public Avatar(String imageUrl, String name) {
    this(imageUrl, name, null);
  }

  public Avatar(String imageUrl, String name, String role) {
    this.imageUrl = imageUrl;
    this.name = name;
    this.role = role;
    setupAvatar();
  }

  private void setupAvatar() {
    container.addClassName("avatar-container");

    // Add role-based ring class
    if (role != null) {
      if (role.contains("ADMIN")) {
        container.addClassName("avatar-admin");
      } else if (role.contains("SUPPORT")) {
        container.addClassName("avatar-support");
      } else {
        container.addClassName("avatar-user");
      }
    }

    if (imageUrl != null && !imageUrl.isEmpty()) {
      Img avatarImg = new Img(imageUrl);
      avatarImg.addClassName("avatar-image");
      avatarImg.setAlt(name);
      avatarImg.setAttribute("referrerpolicy", "no-referrer");
      avatarImg.setAttribute("loading", "lazy");
      container.add(avatarImg);
    }
  }
}
