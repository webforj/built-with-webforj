package com.webforj.builtwithwebforj.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.todo.views")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "webforj-todo", shortName = "webforj-todo")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
