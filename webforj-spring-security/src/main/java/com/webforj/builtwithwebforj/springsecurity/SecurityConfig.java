package com.webforj.builtwithwebforj.springsecurity;

import com.webforj.builtwithwebforj.springsecurity.service.CustomOAuth2UserService;
import com.webforj.builtwithwebforj.springsecurity.service.CustomOidcUserService;
import com.webforj.builtwithwebforj.springsecurity.service.CustomUserDetailsService;
import com.webforj.builtwithwebforj.springsecurity.views.AccessDenyView;
import com.webforj.builtwithwebforj.springsecurity.views.LoginView;
import com.webforj.spring.security.WebforjAuthenticationSuccessHandler;
import com.webforj.spring.security.WebforjSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the application.
 *
 * This configuration supports three authentication methods:
 * 1. Form login - Username/password authentication against the database
 * 2. OAuth2 login - GitHub authentication (non-OIDC provider)
 * 3. OIDC login - Google authentication (OpenID Connect provider)
 *
 * Authentication flow:
 * - Form login: CustomUserDetailsService loads user from database
 * - OAuth2/OIDC: Custom user services create/update users in database on first login
 *
 * Authorization is handled by:
 * - Jakarta @RolesAllowed annotations on route classes
 * - Custom RouteSecurityEvaluators (e.g., TicketOwnershipEvaluator)
 *
 * User roles stored in database: USER, SUPPORT, ADMIN
 * Spring Security expects: ROLE_USER, ROLE_SUPPORT, ROLE_ADMIN (prefix added automatically)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  // Service for form-based login - loads users from database
  @Autowired
  private CustomUserDetailsService userDetailsService;

  // Service for OAuth2 providers like GitHub (non-OIDC)
  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  // Service for OIDC providers like Google (OpenID Connect)
  @Autowired
  private CustomOidcUserService customOidcUserService;

  // webforJ's success handler for proper SPA navigation after login
  @Autowired
  private WebforjAuthenticationSuccessHandler successHandler;

  /**
   * Configures the security filter chain with all authentication methods.
   *
   * The order of configuration matters:
   * 1. webforJ integration (login page, access denied page, logout)
   * 2. Remember-me functionality for persistent sessions
   * 3. OAuth2/OIDC login with custom user services
   */
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        // 1. Configure webforJ security integration
        .with(WebforjSecurityConfigurer.webforj(), configurer -> configurer
            .loginPage(LoginView.class)       // Route to login view for unauthenticated users
            .accessDeniedPage(AccessDenyView.class)  // Route for unauthorized access attempts
            .logout())                         // Enable logout functionality

        // 2. Enable "Remember Me" for persistent login sessions
        .rememberMe(rememberMe -> rememberMe
            .key("uniqueAndSecret")           // Key for token generation (should be externalized)
            .tokenValiditySeconds(86400))     // Token valid for 24 hours

        // 3. Configure OAuth2/OIDC login (Google, GitHub)
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/signin")             // Redirect to our custom login page
            .successHandler(successHandler)   // Handle successful OAuth login for webforJ SPA
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)    // GitHub (OAuth2)
                .oidcUserService(customOidcUserService))) // Google (OIDC)
        .build();
  }

  /**
   * Password encoder for secure password storage.
   * BCrypt automatically handles salting and is resistant to brute-force attacks.
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Authentication manager for form-based login.
   * Uses DaoAuthenticationProvider to authenticate against database users.
   */
  @Bean
  AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authenticationProvider);
  }
}
