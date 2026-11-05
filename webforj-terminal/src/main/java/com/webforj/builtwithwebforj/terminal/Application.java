package com.webforj.builtwithwebforj.terminal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.terminal.views")
@BundleEntry("app.css")
@AppTheme("system")
@AppProfile(name = "Terminal", shortName = "Terminal")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
