package com.webforj.builtwithwebforj.ui5;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the app. The stylesheet holds the page layout, and it loads once at app start.
 */
@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.ui5.views")
@BundleEntry("app.css")
@AppTheme("light")
@AppProfile(name = "UI5 Customers", shortName = "UI5")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
