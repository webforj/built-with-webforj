package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Span;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserInfo extends Composite<Span> {

  private final Span container = getBoundComponent();

  public UserInfo() {
    setupUserInfo();
  }

  private void setupUserInfo() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && auth.isAuthenticated()) {
      String displayName = extractDisplayName(auth);
      String avatarUrl = extractAvatarUrl(auth);
      String role = extractRole(auth);

      if (displayName != null) {
        container.addClassName("user-info-container");

        Avatar avatar = new Avatar(avatarUrl, displayName, role);
        container.add(avatar);

        Span userSpan = new Span(displayName);
        userSpan.addClassName("toolbar-username");
        container.add(userSpan);
      }
    }
  }

  private String extractRole(Authentication auth) {
    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      return "ADMIN";
    } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPPORT"))) {
      return "SUPPORT";
    }
    return "USER";
  }

  private String extractDisplayName(Authentication auth) {
    if (auth.getPrincipal() instanceof OAuth2User) {
      OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();

      // Try Google attributes first
      String googleName = oauth2User.getAttribute("name");
      if (googleName != null) {
        return googleName;
      }

      // Try GitHub attributes
      String githubLogin = oauth2User.getAttribute("login");
      if (githubLogin != null) {
        return githubLogin;
      }

      // Fallback to the default name
      return oauth2User.getName();
    } else {
      // For form login users
      return auth.getName();
    }
  }

  private String extractAvatarUrl(Authentication auth) {
    if (auth.getPrincipal() instanceof OAuth2User) {
      OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();

      // Try Google picture
      String googlePicture = oauth2User.getAttribute("picture");
      if (googlePicture != null) {
        return googlePicture;
      }

      // Try GitHub avatar
      String githubAvatar = oauth2User.getAttribute("avatar_url");
      if (githubAvatar != null) {
        return githubAvatar;
      }
    }

    // Generate avatar for form login users using UI Avatars API
    String displayName = extractDisplayName(auth);
    if (displayName != null) {
      String encodedName = displayName.replace(" ", "+");
      return "https://ui-avatars.com/api/?name=" + encodedName +
             "&background=4A9EFF&color=fff&size=40";
    }

    return null;
  }
}
