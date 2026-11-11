package com.webforj.builtwithwebforj.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppMeta;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.crud.views")
@BundleEntry("app.css")
@AppTheme("system")
@AppMeta(name = "viewport",
    content = "width=device-width, initial-scale=1.0, viewport-fit=cover")
@AppProfile(name = "CRUD", shortName = "CRUD")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
