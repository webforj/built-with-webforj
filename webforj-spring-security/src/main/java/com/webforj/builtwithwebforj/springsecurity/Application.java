package com.webforj.builtwithwebforj.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.springsecurity.views")
@StyleSheet("ws://app.css")
@AppProfile(name = "webforj-spring-security", shortName = "webforj-spring-security")
@AppTheme("dark")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
