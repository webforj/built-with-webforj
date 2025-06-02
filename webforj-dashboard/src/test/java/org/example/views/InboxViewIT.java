package org.example.views;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

class InboxViewIT {

  static Playwright playwright = Playwright.create();
  Browser browser;
  Page page;
  String port = System.getProperty("server.port", "8080");

  @BeforeEach
  void setUp() {
    // By default, Playwright runs the browsers in headless mode. To see the browser
    // UI, setHeadless option to false. You can also use setSlowMo to slow down
    // execution. Learn more in the debugging tools section.
    // https://playwright.dev/java/docs/debug

    // browser = playwright.chromium().launch(new
    // BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));

    browser = playwright.chromium().launch();
    page = browser.newPage();
    page.navigate("http://localhost:" + port + "/");
  }

  @Test
  void shouldRenderPage() {
    assertThat(page.locator(".explore-component"))
        .containsText("Inbox");
  }
}
