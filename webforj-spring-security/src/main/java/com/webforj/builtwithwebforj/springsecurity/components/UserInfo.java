package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.component.Composite;
import com.webforj.component.avatar.Avatar;
import com.webforj.component.avatar.AvatarExpanse;
import com.webforj.component.html.elements.Img;
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

        Avatar avatar = createAvatar(displayName, avatarUrl, role);
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

      // Try email
      String email = oauth2User.getAttribute("email");
      if (email != null) {
        return email.split("@")[0];
      }

      // Fallback to the default name (safely)
      try {
        return oauth2User.getName();
      } catch (Exception e) {
        return "User";
      }
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

  private Avatar createAvatar(String displayName, String avatarUrl, String role) {
    Avatar avatar = new Avatar(displayName);
    avatar.setExpanse(AvatarExpanse.SMALL);
    avatar.addClassName("toolbar-avatar");

    // Add role-based styling class
    if (role != null) {
      if (role.contains("ADMIN")) {
        avatar.addClassName("avatar-admin");
      } else if (role.contains("SUPPORT")) {
        avatar.addClassName("avatar-support");
      } else {
        avatar.addClassName("avatar-user");
      }
    }

    // Add image if URL is provided
    if (avatarUrl != null && !avatarUrl.isEmpty()) {
      Img avatarImg = new Img(avatarUrl);
      avatarImg.setAttribute("referrerpolicy", "no-referrer");
      avatarImg.setAttribute("loading", "lazy");
      avatar.add(avatarImg);
    }

    return avatar;
  }
}
