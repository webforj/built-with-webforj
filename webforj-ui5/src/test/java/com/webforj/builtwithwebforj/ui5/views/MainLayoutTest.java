package com.webforj.builtwithwebforj.ui5.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.ui5.Application;
import com.webforj.builtwithwebforj.ui5.components.Ui5Button;
import com.webforj.builtwithwebforj.ui5.components.Ui5ShellBar;
import com.webforj.builtwithwebforj.ui5.components.Ui5SideNavigation;
import com.webforj.router.history.ParametersBag;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(app = Application.class)
class MainLayoutTest {

  @Test
  void shouldHighlightTheSectionOfTheCurrentRoute(WebforjTestbed context) {
    context.navigate(CustomersView.class);
    assertEquals("customers", navigation().getSelectedKey());

    context.navigate(CustomerFormView.class, ParametersBag.of("id=new"));
    assertEquals("customers", navigation().getSelectedKey());

    context.navigate(AboutView.class);
    assertEquals("about", navigation().getSelectedKey());
  }

  @Test
  void shouldOpenTheRouteOfThePickedItem(WebforjTestbed context) {
    context.navigate(CustomersView.class);

    // The navigation reports a click with the key of the item, which is the route path.
    UserInteraction.fireEvent(navigation(), "click", Map.of("key", "about"));

    assertTrue(ComponentLocator.of(AboutView.class).isPresent());
  }

  @Test
  void shouldCollapseTheNavigationFromTheMenuButton(WebforjTestbed context) {
    context.navigate(CustomersView.class);
    Ui5ShellBar shellBar = ComponentLocator.of(Ui5ShellBar.class).locate();

    UserInteraction.click(ComponentLocator.of(Ui5Button.class).within(shellBar).locate());

    assertTrue(navigation().isCollapsed());
  }

  private static Ui5SideNavigation navigation() {
    return ComponentLocator.of(Ui5SideNavigation.class).locate();
  }
}
