package com.webforj.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

/**
 * The entry point of the WebforJ Bookstore application.
 * <p>
 * This class configures the Spring Boot application and defines the WebforJ
 * application settings
 * such as routing packages, styling, and theme.
 * </p>
 * 
 * @author webforJ Bookstore
 */
@SpringBootApplication
@Routify(packages = "com.webforj.bookstore.views")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "webforj-bookstore", shortName = "webforj-bookstore")
public class Application extends App {

  /**
   * Main method to start the Spring Boot application.
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
