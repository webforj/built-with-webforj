package com.webforjspring.views;

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
class HelloWorldViewIT {

  static Playwright playwright = Playwright.create();
  Browser browser;
  Page page;
  
  @LocalServerPort
  private int port;

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

  @AfterEach
  void tearDown() {
    if (browser != null) {
      browser.close();
    }
  }

  @Test
  void shouldClickButton() {
    page.locator("input").fill("John Doe");
    page.getByText("Say Hello").click();

    assertThat(page.locator("dwc-toast").first())
        .containsText("Welcome to webforJ Starter John Doe!");
  }
}