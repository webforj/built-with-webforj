package com.webforj.builtwithwebforj.focustracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.focustracker.views")
@StyleSheet("ws://theme.css")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "Focus Tracker", shortName = "Focus")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
