package com.webforjgeolocation.views;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WondersViewIT {

  static Playwright playwright = Playwright.create();
  Browser browser;
  Page page;

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    browser = playwright.chromium().launch();
    page = browser.newPage();
    page.navigate("http://localhost:" + port + "/");
  }

  @AfterEach
  void tearDown() {
    if (browser != null) {
      browser.close();
    }
  }

  @Test
  void shouldRenderMasthead() {
    assertThat(page.locator(".masthead h1")).containsText("Wonders of the World");
  }
}
