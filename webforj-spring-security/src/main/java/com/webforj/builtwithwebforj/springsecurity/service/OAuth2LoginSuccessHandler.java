package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Handles successful OAuth2 login by creating or updating the user in the database.
 * Extends SavedRequestAwareAuthenticationSuccessHandler to preserve webforJ's
 * request tracking and redirect behavior.
 */
@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final UserRepository userRepository;

  public OAuth2LoginSuccessHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

    // Extract user info from OAuth2 provider
    String email = oauth2User.getAttribute("email");
    String name = oauth2User.getAttribute("name");

    // Handle case where email might not be provided
    if (email == null || email.isBlank()) {
      // Try to get login/username for GitHub users
      String login = oauth2User.getAttribute("login");
      if (login != null) {
        email = login + "@oauth.user";
      } else {
        email = "oauth_" + UUID.randomUUID().toString().substring(0, 8) + "@oauth.user";
      }
    }

    // Check if user exists by email
    final String userEmail = email;
    User user = userRepository.findByEmail(userEmail).orElse(null);

    if (user == null) {
      // Also check by username (in case email is used as username)
      user = userRepository.findByUsername(userEmail).orElse(null);
    }

    if (user == null) {
      // Create new user
      user = new User();
      user.setUsername(userEmail);
      user.setEmail(userEmail);
      user.setDisplayName(name != null ? name : userEmail.split("@")[0]);
      // OAuth users don't need a password - set a random unusable value
      user.setPassword(UUID.randomUUID().toString());
      // Assign default USER role
      user.addRole("USER");
      userRepository.save(user);

      System.out.println("Created new OAuth2 user: " + userEmail + " with role USER");
    } else {
      // Optionally update display name if it changed
      if (name != null && !name.equals(user.getDisplayName())) {
        user.setDisplayName(name);
        userRepository.save(user);
        System.out.println("Updated OAuth2 user display name: " + userEmail);
      }
    }

    // Continue with the default redirect behavior
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
