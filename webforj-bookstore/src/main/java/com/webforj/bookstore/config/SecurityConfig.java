package com.webforj.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.webforj.bookstore.views.AccessDenyView;
import com.webforj.bookstore.views.LoginView;
import com.webforj.spring.security.WebforjSecurityConfigurer;

/**
 * Security configuration for the application.
 * <p>
 * Configures the security filter chain, authentication manager, and user
 * details service
 * using Spring Security. It sets up in-memory authentication for demonstration
 * purposes.
 * </p>
 *
 * @author webforJ Bookstore
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * 
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring the security chain
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .with(WebforjSecurityConfigurer.webforj(), configurer -> configurer
                        .loginPage(LoginView.class)
                        .accessDeniedPage(AccessDenyView.class)
                        .logout())
                .build();
    }

    /**
     * Provides the password encoder.
     * 
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an in-memory user details service with sample users.
     * 
     * @param passwordEncoder the password encoder to use for encoding passwords
     * @return the UserDetailsService containing the configured users
     */
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Configures the authentication manager.
     * 
     * @param userDetailsService the user details service to use
     * @param passwordEncoder    the password encoder to use
     * @return the configured AuthenticationManager
     */
    @Bean
    AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }
}