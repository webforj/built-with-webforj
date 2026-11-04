package com.webforj.builtwithwebforj.smartform;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Routify(packages = "com.webforj.builtwithwebforj.smartform.views")
@BundleEntry("app.css")
@AppTheme("system")
@AppProfile(name = "SmartForm", shortName = "SmartForm")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
