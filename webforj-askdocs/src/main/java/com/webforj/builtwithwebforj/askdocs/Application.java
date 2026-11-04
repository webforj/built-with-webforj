package com.webforj.builtwithwebforj.askdocs;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.askdocs.views")
@BundleEntry("app.css")
@AppTheme("system")
@AppProfile(name = "AskDocs", shortName = "AskDocs")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
