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
 * Security configuration using database-backed authentication.
 * Users are loaded from H2 database via CustomUserDetailsService.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  @Autowired
  private CustomOidcUserService customOidcUserService;

  @Autowired
  private WebforjAuthenticationSuccessHandler successHandler;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .with(WebforjSecurityConfigurer.webforj(), configurer -> configurer
            .loginPage(LoginView.class)
            .accessDeniedPage(AccessDenyView.class)
            .logout())
        .rememberMe(rememberMe -> rememberMe
            .key("uniqueAndSecret")
            .tokenValiditySeconds(86400)) // 24 hours
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/signin")
            .successHandler(successHandler)
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService)))
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authenticationProvider);
  }
}
