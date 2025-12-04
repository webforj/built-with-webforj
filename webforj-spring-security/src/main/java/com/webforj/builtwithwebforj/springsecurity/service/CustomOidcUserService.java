package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Custom OidcUserService for OpenID Connect providers (like Google).
 * Creates/updates users in the database and assigns proper authorities from database roles.
 */
@Service
public class CustomOidcUserService extends OidcUserService {

  private final UserRepository userRepository;

  public CustomOidcUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);

    // Extract user info from OIDC provider
    String email = oidcUser.getEmail();
    String name = oidcUser.getFullName();

    // Handle case where email might not be provided
    if (email == null || email.isBlank()) {
      String subject = oidcUser.getSubject();
      email = "oidc_" + subject.substring(0, Math.min(8, subject.length())) + "@oauth.user";
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
      user.setDisplayName(name != null ? name : userEmail.split("@")[0]);
      user.setPassword(UUID.randomUUID().toString());
      user.addRole("USER");
      user = userRepository.save(user);
    } else if (name != null && !name.equals(user.getDisplayName())) {
      // Update display name if changed
      user.setDisplayName(name);
      user = userRepository.save(user);
    }

    // Build authorities from database roles
    Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());
    for (String role : user.getRoles()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Return OidcUser with merged authorities
    // Use "email" as the name attribute since that's what we use as username in the database
    // Note: For OIDC, email is reliably available from the ID token
    return new DefaultOidcUser(
        authorities,
        oidcUser.getIdToken(),
        oidcUser.getUserInfo(),
        "email"
    );
  }
}
