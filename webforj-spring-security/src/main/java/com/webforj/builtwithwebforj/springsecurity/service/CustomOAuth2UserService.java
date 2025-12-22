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
 * Custom OAuth2UserService for non-OIDC OAuth2 providers (e.g., GitHub).
 *
 * This service is called when a user authenticates via OAuth2 (not OIDC).
 * It bridges the gap between OAuth2 provider data and our application's user database.
 *
 * Key responsibilities:
 * 1. Extract user information from the OAuth2 provider response
 * 2. Create a new user in our database on first login (auto-registration)
 * 3. Update user display name if it changed at the provider
 * 4. Merge OAuth2 authorities with database roles
 * 5. Ensure consistent username (email) for database lookups
 *
 * GitHub-specific handling:
 * - GitHub may hide user emails (privacy setting)
 * - Falls back to "login@github.user" if email is not available
 * - Uses GitHub "login" (username) for display name if "name" is not set
 *
 * Why we use email as the principal name:
 * - Provides a consistent identifier across OAuth2 and form login
 * - Allows looking up the user in our database via auth.getName()
 * - Email is the most reliable cross-provider identifier
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    // 1. Load user info from OAuth2 provider (GitHub, etc.)
    OAuth2User oauth2User = super.loadUser(userRequest);

    // 2. Extract user attributes from provider response
    String email = oauth2User.getAttribute("email");
    String name = oauth2User.getAttribute("name");
    String login = oauth2User.getAttribute("login"); // GitHub-specific: the username

    // 3. Handle missing email (GitHub allows users to hide their email)
    if (email == null || email.isBlank()) {
      if (login != null) {
        // Use GitHub username as a pseudo-email
        email = login + "@github.user";
      } else {
        // Last resort: generate a random identifier
        email = "oauth_" + UUID.randomUUID().toString().substring(0, 8) + "@oauth.user";
      }
    }

    // 4. Determine display name (prefer "name", fall back to "login" or email prefix)
    if (name == null || name.isBlank()) {
      name = login != null ? login : email.split("@")[0];
    }

    // 5. Find existing user or create new one (auto-registration)
    final String userEmail = email;
    User user = userRepository.findByEmail(userEmail)
        .orElseGet(() -> userRepository.findByUsername(userEmail).orElse(null));

    if (user == null) {
      // First-time login: create new user with USER role
      user = new User();
      user.setUsername(userEmail);
      user.setEmail(userEmail);
      user.setDisplayName(name);
      user.setPassword(UUID.randomUUID().toString()); // Random password (won't be used for OAuth)
      user.addRole("USER");
      user = userRepository.save(user);
    } else if (name != null && !name.equals(user.getDisplayName())) {
      // Returning user: update display name if changed at provider
      user.setDisplayName(name);
      user = userRepository.save(user);
    }

    // 6. Merge authorities: OAuth2 scopes + database roles
    Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());
    for (String role : user.getRoles()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // 7. Build attributes map with our email as the identifier
    // This ensures auth.getName() returns the email we use as username
    Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
    attributes.put("email", userEmail);

    // 8. Return OAuth2User with merged authorities and email as principal name
    return new DefaultOAuth2User(
        authorities,
        attributes,
        "email"  // Use email as the name attribute for auth.getName()
    );
  }
}
