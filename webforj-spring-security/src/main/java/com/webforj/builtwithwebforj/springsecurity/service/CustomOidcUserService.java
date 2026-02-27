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
 * Custom OidcUserService for OpenID Connect providers (e.g., Google).
 *
 * This service is called when a user authenticates via OIDC (OpenID Connect).
 * OIDC is an identity layer on top of OAuth2 that provides standardized user info.
 *
 * Key differences from OAuth2 (CustomOAuth2UserService):
 * - OIDC provides an ID token with verified claims (email, name, etc.)
 * - Email is typically more reliable in OIDC than plain OAuth2
 * - User info comes from both ID token and UserInfo endpoint
 *
 * Key responsibilities:
 * 1. Extract user information from OIDC ID token and UserInfo
 * 2. Create a new user in our database on first login (auto-registration)
 * 3. Update user display name if it changed at the provider
 * 4. Merge OIDC authorities with database roles
 * 5. Ensure consistent username (email) for database lookups
 *
 * Google-specific notes:
 * - Google always provides email (required scope)
 * - Full name is available via getFullName()
 * - Profile picture available via getPicture() (used in UI)
 *
 * Why we use email as the principal name:
 * - Provides a consistent identifier across OIDC and form login
 * - Allows looking up the user in our database via auth.getName()
 * - OIDC email claims are verified by the provider
 */
@Service
public class CustomOidcUserService extends OidcUserService {

  private final UserRepository userRepository;

  public CustomOidcUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    // 1. Load user info from OIDC provider (Google, etc.)
    //    This includes both ID token claims and UserInfo endpoint data
    OidcUser oidcUser = super.loadUser(userRequest);

    // 2. Extract user attributes from OIDC claims
    String email = oidcUser.getEmail();        // From ID token or UserInfo
    String name = oidcUser.getFullName();      // From ID token or UserInfo

    // 3. Handle missing email (rare for OIDC, but possible)
    if (email == null || email.isBlank()) {
      // Fall back to subject identifier (unique per provider)
      String subject = oidcUser.getSubject();
      email = "oidc_" + subject.substring(0, Math.min(8, subject.length())) + "@oauth.user";
    }

    // 4. Find existing user or create new one (auto-registration)
    final String userEmail = email;
    User user = userRepository.findByEmail(userEmail)
        .orElseGet(() -> userRepository.findByUsername(userEmail).orElse(null));

    if (user == null) {
      // First-time login: create new user with USER role
      user = new User();
      user.setUsername(userEmail);
      user.setEmail(userEmail);
      user.setDisplayName(name != null ? name : userEmail.split("@")[0]);
      user.setPassword(UUID.randomUUID().toString()); // Random password (won't be used for OIDC)
      user.addRole("USER");
      user = userRepository.save(user);
    } else if (name != null && !name.equals(user.getDisplayName())) {
      // Returning user: update display name if changed at provider
      user.setDisplayName(name);
      user = userRepository.save(user);
    }

    // 5. Merge authorities: OIDC scopes + database roles
    Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());
    for (String role : user.getRoles()) {
      authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // 6. Return OidcUser with merged authorities and email as principal name
    //    Unlike OAuth2, OIDC preserves the ID token and UserInfo for later access
    return new DefaultOidcUser(
        authorities,
        oidcUser.getIdToken(),    // Preserve ID token (contains verified claims)
        oidcUser.getUserInfo(),   // Preserve UserInfo (additional profile data)
        "email"                    // Use email as the name attribute for auth.getName()
    );
  }
}
