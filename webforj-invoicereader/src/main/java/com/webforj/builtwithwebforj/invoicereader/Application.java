package com.webforj.builtwithwebforj.invoicereader;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Routify(packages = "com.webforj.builtwithwebforj.invoicereader.views")
@BundleEntry("app.css")
@AppTheme("system")
@AppProfile(name = "InvoiceReader", shortName = "InvoiceReader")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // Spring AI auto-configures a ChatClient.Builder. The view receives the built client.
  @Bean
  ChatClient chatClient(ChatClient.Builder builder) {
    return builder.build();
  }
}
