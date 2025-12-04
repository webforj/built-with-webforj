package com.webforj.builtwithwebforj.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

/**
 * Main Spring Boot application class.
 * 
 * @SpringBootApplication combines three annotations:
 * - @Configuration: Marks this as a configuration class
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Scans for Spring components in this package and below
 */
@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.dashboard.views")
@StyleSheet("ws://app.css")
@StyleSheet("ws://dashboard-view.css")
@AppTheme("light")
@AppProfile(name = "webforj-dashboard", shortName = "webforj-dashboard")
public class Application extends App {

  /**
   * Main method - Entry point for Spring Boot application.
   * SpringApplication.run() bootstraps the Spring context and starts the embedded server.
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
