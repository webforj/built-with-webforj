package com.webforj.builtwithwebforj.ui5.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.ui5.Application;
import com.webforj.builtwithwebforj.ui5.components.Ui5Text;
import com.webforj.builtwithwebforj.ui5.components.Ui5Title;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(app = Application.class)
class AboutViewTest {

  @Test
  void shouldExplainTheApp(WebforjTestbed context) {
    context.navigate(AboutView.class);
    AboutView view = ComponentLocator.of(AboutView.class).locate();

    assertEquals("A webforJ app in SAP UI5",
        ComponentLocator.of(Ui5Title.class).within(view).locate().getText());
    assertTrue(ComponentLocator.of(Ui5Text.class).within(view).count() > 0);
  }
}
