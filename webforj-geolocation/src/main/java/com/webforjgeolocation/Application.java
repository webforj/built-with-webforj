package com.webforjgeolocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@SpringBootApplication
@Routify(packages = "com.webforjgeolocation.views")
@StyleSheet("https://fonts.googleapis.com/css2?family=Cinzel:wght@500;600;700&family=EB+Garamond:ital,wght@0,400;0,500;1,400;1,500&family=Cutive+Mono&display=swap")
@StyleSheet("ws://css/tokens.css")
@StyleSheet("ws://css/styles.css")
@StyleSheet("ws://css/emblems.css")
@StyleSheet("ws://app.css")
@AppTheme("light")
@AppProfile(name = "Wonders of the World", shortName = "Wonders")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
