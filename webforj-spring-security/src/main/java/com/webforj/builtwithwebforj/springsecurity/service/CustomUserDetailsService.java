package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom UserDetailsService for form-based (username/password) authentication.
 *
 * This service is called by Spring Security's DaoAuthenticationProvider when
 * a user attempts to log in via the form login page with username and password.
 *
 * Key responsibilities:
 * 1. Load user from database by username
 * 2. Convert database roles to Spring Security GrantedAuthority objects
 * 3. Return a UserDetails object for password verification
 *
 * How it fits in the authentication flow:
 * 1. User submits username/password on login form
 * 2. DaoAuthenticationProvider calls loadUserByUsername()
 * 3. This service loads user from database and returns UserDetails
 * 4. DaoAuthenticationProvider compares submitted password with stored hash
 * 5. If match, user is authenticated with the returned authorities
 *
 * Role format:
 * - Database stores: "USER", "SUPPORT", "ADMIN"
 * - Spring Security expects: "ROLE_USER", "ROLE_SUPPORT", "ROLE_ADMIN"
 * - This service adds the "ROLE_" prefix when building authorities
 *
 * Note: This service is NOT used for OAuth2/OIDC logins.
 * Those use CustomOAuth2UserService and CustomOidcUserService respectively.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 1. Load user from database
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username));

    // 2. Convert database roles to Spring Security authorities
    //    Database stores "USER" -> Spring Security needs "ROLE_USER"
    Set<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toSet());

    // 3. Return Spring Security's UserDetails for authentication
    //    The password will be verified by DaoAuthenticationProvider using BCrypt
    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())  // BCrypt-hashed password from database
        .authorities(authorities)
        .build();
  }
}
