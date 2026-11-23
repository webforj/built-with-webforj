package com.webforj.builtwithwebforj.ui5.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.ui5.Application;
import com.webforj.builtwithwebforj.ui5.components.Ui5Input;
import com.webforj.builtwithwebforj.ui5.components.Ui5Table;
import com.webforj.builtwithwebforj.ui5.customers.Customer;
import com.webforj.builtwithwebforj.ui5.customers.CustomerService;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Drives the view the way a user does, without a browser or a server.
@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(app = Application.class)
class CustomersViewTest {

  @Autowired
  private CustomerService service;

  @Test
  void shouldListTheCustomersSortedByName(WebforjTestbed context) {
    context.navigate(CustomersView.class);

    assertEquals(service.search("").size(), names().size());
    assertEquals("Ada Lovelace", names().get(0));
  }

  @Test
  void shouldSearchOnceTypingPauses(WebforjTestbed context) {
    context.navigate(CustomersView.class);
    Ui5Input search = ComponentLocator.of(Ui5Input.class).byLabel("Search customers").locate();

    UserInteraction.fireEvent(search, "input", Map.of("value", "grace"));
    // The search is debounced, so nothing has happened yet.
    assertTrue(names().size() > 1);

    // Lets the debounce delay elapse.
    context.runIntervals();
    assertEquals(List.of("Grace Hopper"), names());
  }

  @Test
  void shouldOpenTheCustomerOfTheClickedRow(WebforjTestbed context) {
    context.navigate(CustomersView.class);

    // The table reports a click with the key of the row, which is its position.
    UserInteraction.fireEvent(table(), "row-click", Map.of("rowKey", "0"));

    assertTrue(ComponentLocator.of(CustomerFormView.class).isPresent());
    assertEquals("Ada Lovelace",
        ComponentLocator.of(Ui5Input.class).byLabel("Name").locate().getValue());
  }

  @SuppressWarnings("unchecked")
  private static Ui5Table<Customer> table() {
    return ComponentLocator.of(Ui5Table.class).locate();
  }

  private static List<String> names() {
    return table().getItems().stream().map(Customer::getName).toList();
  }
}
