package com.webforj.builtwithwebforj.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.crud.views")
@StyleSheet("ws://app.css")
@AppTheme("system")
@AppProfile(name = "webforj-crud", shortName = "webforj-crud")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
