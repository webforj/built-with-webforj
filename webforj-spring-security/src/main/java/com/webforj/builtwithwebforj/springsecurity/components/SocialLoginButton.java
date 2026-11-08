package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.Page;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Img;
import com.webforj.component.html.elements.NativeButton;
import com.webforj.component.html.elements.Span;

public class SocialLoginButton extends Composite<NativeButton> {

  public enum Provider {
    GOOGLE("Continue with Google",
        "https://www.svgrepo.com/show/475656/google-color.svg",
        "/oauth2/authorization/google"),
    GITHUB("Continue with GitHub",
        "https://www.svgrepo.com/show/475654/github-color.svg",
        "/oauth2/authorization/github");

    private final String displayName;
    private final String iconUrl;
    private final String authUrl;

    Provider(String displayName, String iconUrl, String authUrl) {
      this.displayName = displayName;
      this.iconUrl = iconUrl;
      this.authUrl = authUrl;
    }

    public String getDisplayName() {
      return displayName;
    }

    public String getIconUrl() {
      return iconUrl;
    }

    public String getAuthUrl() {
      return authUrl;
    }
  }

  private final NativeButton button = getBoundComponent();

  public SocialLoginButton(Provider provider) {
    button.addClassName("social-login-button");
    button.addClassName("social-login-" + provider.name().toLowerCase());

    Img icon = new Img(provider.getIconUrl());
    icon.setSize("20px", "20px");
    icon.addClassName("social-login-icon");

    Span text = new Span(provider.getDisplayName());

    button.add(icon, text);

    button.onClick(e -> {
      Page.getCurrent().open(provider.getAuthUrl(), "_self");
    });
  }
}
