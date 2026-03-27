package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for user management operations.
 *
 * This service provides the business logic layer between views and the UserRepository.
 * All user-related operations should go through this service to ensure:
 * 1. Proper transaction management
 * 2. Password encoding for new users
 * 3. Business rule validation (e.g., unique usernames)
 * 4. Consistent error handling
 *
 * Note: This service is for user MANAGEMENT (CRUD operations).
 * For authentication, see:
 * - CustomUserDetailsService (form login)
 * - CustomOAuth2UserService (GitHub)
 * - CustomOidcUserService (Google)
 */
@Service
@Transactional
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Get all users in the system.
   * Used by admin views to display user lists.
   */
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Find a user by their ID.
   *
   * @param id the user ID
   * @return Optional containing the user if found
   */
  @Transactional(readOnly = true)
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  /**
   * Find a user by their username.
   * Username is used as the principal identifier across all auth methods.
   *
   * @param username the username to search for
   * @return Optional containing the user if found
   */
  @Transactional(readOnly = true)
  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Get a user by username, throwing an exception if not found.
   * Useful when the user is expected to exist (e.g., current authenticated user).
   *
   * @param username the username to search for
   * @return the user
   * @throws RuntimeException if user not found
   */
  @Transactional(readOnly = true)
  public User getRequiredUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found: " + username));
  }

  /**
   * Check if a username is already taken.
   *
   * @param username the username to check
   * @return true if username exists, false otherwise
   */
  @Transactional(readOnly = true)
  public boolean usernameExists(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  /**
   * Create a new user with encoded password.
   * The password will be hashed using BCrypt before storage.
   *
   * @param user the user to create (with plain text password)
   * @return the saved user
   * @throws IllegalArgumentException if username already exists
   */
  public User createUser(User user) {
    // 1. Validate username uniqueness
    if (usernameExists(user.getUsername())) {
      throw new IllegalArgumentException("Username already exists: " + user.getUsername());
    }

    // 2. Encode password before saving
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    // 3. Save and return
    return userRepository.save(user);
  }

  /**
   * Update an existing user.
   * Note: This method does NOT update the password. Use updatePassword() for that.
   *
   * @param user the user to update
   * @return the updated user
   * @throws IllegalArgumentException if changing username to one that already exists
   */
  public User updateUser(User user) {
    // 1. Check if username is being changed to an existing one
    Optional<User> existingWithUsername = userRepository.findByUsername(user.getUsername());
    if (existingWithUsername.isPresent() && !existingWithUsername.get().getId().equals(user.getId())) {
      throw new IllegalArgumentException("Username already exists: " + user.getUsername());
    }

    // 2. Save and return
    return userRepository.save(user);
  }

  /**
   * Update a user's password.
   *
   * @param userId the user ID
   * @param newPassword the new plain text password (will be encoded)
   * @throws RuntimeException if user not found
   */
  public void updatePassword(Long userId, String newPassword) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodedPassword);
    userRepository.save(user);
  }

  /**
   * Delete a user from the system.
   *
   * @param user the user to delete
   */
  public void deleteUser(User user) {
    userRepository.delete(user);
  }

  /**
   * Delete a user by their ID.
   *
   * @param id the user ID
   * @throws RuntimeException if user not found
   */
  public void deleteUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    userRepository.delete(user);
  }
}
