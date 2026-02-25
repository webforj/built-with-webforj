package com.webforj.builtwithwebforj.ghostai;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.JavaScript;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.ghostai.views")
@StyleSheet("ws://app.css")
@StyleSheet("ws://prism-theme.css")
@JavaScript(value = "https://cdn.jsdelivr.net/combine/npm/prismjs@1/prism.min.js,npm/prismjs@1/plugins/autoloader/prism-autoloader.min.js", top = true)
@AppProfile(name = "ghost:ai", shortName = "ghost:ai")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean(destroyMethod = "shutdown")
  ScheduledExecutorService debouncerExecutor() {
    return Executors.newScheduledThreadPool(2);
  }
}
