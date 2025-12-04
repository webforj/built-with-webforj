package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom OAuth2UserService that creates/updates users in the database
 * and assigns proper authorities from the database roles.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);

    // Extract user info from OAuth2 provider
    String email = oauth2User.getAttribute("email");
    String name = oauth2User.getAttribute("name");
    String login = oauth2User.getAttribute("login"); // GitHub-specific

    // Handle case where email might not be provided (GitHub can hide emails)
    if (email == null || email.isBlank()) {
      if (login != null) {
        email = login + "@github.user";
      } else {
        email = "oauth_" + UUID.randomUUID().toString().substring(0, 8) + "@oauth.user";
      }
    }

    // For GitHub, prefer the login (username) for display if name is not set
    if (name == null || name.isBlank()) {
      name = login != null ? login : email.split("@")[0];
    }

    // Find or create user in database
    final String userEmail = email;
    User user = userRepository.findByEmail(userEmail)
        .orElseGet(() -> userRepository.findByUsername(userEmail).orElse(null));

    if (user == null) {
      // Create new user
      user = new User();
      user.setUsername(userEmail);
      user.setEmail(userEmail);
      user.setDisplayName(name);
      user.setPassword(UUID.randomUUID().toString());
      user.addRole("USER");
      user = userRepository.save(user);
    } else if (name != null && !name.equals(user.getDisplayName())) {
      // Update display name if changed
      user.setDisplayName(name);
      user = userRepository.save(user);
    }

    // Build authorities from database roles
    Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());
    for (String role : user.getRoles()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Create a new attributes map with the email we're using as username
    // This ensures auth.getName() returns a value we can look up in the database
    Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
    // Ensure email attribute is set to what we use as username
    attributes.put("email", userEmail);

    // Return OAuth2User with merged authorities, using email as the name attribute
    return new DefaultOAuth2User(
        authorities,
        attributes,
        "email"
    );
  }
}
