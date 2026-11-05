package com.webforj.builtwithwebforj.tablesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import com.webforj.App;
import com.webforj.annotation.AppMeta;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;

@SpringBootApplication
@EnableAsync
@Routify(packages = "com.webforj.builtwithwebforj.tablesearch.views")
@BundleEntry("app.css")
@AppTheme("system")
@AppMeta(name = "viewport",
    content = "width=device-width, initial-scale=1.0, viewport-fit=cover")
@AppProfile(name = "Table Search", shortName = "Table Search")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
