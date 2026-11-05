package com.webforj.builtwithwebforj.terminal.views;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.terminal.Application;
import com.webforj.component.button.Button;
import com.webforj.component.terminal.Terminal;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(app = Application.class)
class HomeViewTest {

  @Test
  void shouldOpenTheTerminal(WebforjTestbed context) {
    context.navigate(HomeView.class);

    UserInteraction.click(ComponentLocator.of(Button.class).byText("Open Terminal").locate());

    assertTrue(ComponentLocator.of(Terminal.class).isPresent());
  }
}
