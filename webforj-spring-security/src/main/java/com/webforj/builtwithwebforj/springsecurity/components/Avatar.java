package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Img;

public class Avatar extends Composite<Div> {

  private final Div container = getBoundComponent();
  private String imageUrl;
  private String name;

  public Avatar(String imageUrl, String name) {
    this.imageUrl = imageUrl;
    this.name = name;
    setupAvatar();
  }

  private void setupAvatar() {
    container.addClassName("avatar-container");

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
