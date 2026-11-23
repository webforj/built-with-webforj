package com.webforj.builtwithwebforj.ui5.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.ui5.Application;
import com.webforj.builtwithwebforj.ui5.components.Ui5Button;
import com.webforj.builtwithwebforj.ui5.components.Ui5DatePicker;
import com.webforj.builtwithwebforj.ui5.components.Ui5Dialog;
import com.webforj.builtwithwebforj.ui5.components.Ui5Input;
import com.webforj.builtwithwebforj.ui5.components.Ui5Select;
import com.webforj.builtwithwebforj.ui5.components.Ui5Toast;
import com.webforj.builtwithwebforj.ui5.customers.Customer;
import com.webforj.builtwithwebforj.ui5.customers.CustomerService;
import com.webforj.builtwithwebforj.ui5.customers.Status;
import com.webforj.router.history.ParametersBag;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Drives the form the way a user does, without a browser or a server.
@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(app = Application.class)
class CustomerFormViewTest {

  @Autowired
  private CustomerService service;

  private long id;

  @BeforeEach
  void createCustomer() {
    id = service.save(new Customer("Edsger Dijkstra", "edsger@goto.dev", Status.ACTIVE,
        LocalDate.of(2024, 4, 2), true));
  }

  @Test
  void shouldShowTheCustomerInTheFields(WebforjTestbed context) {
    open(context, String.valueOf(id));

    assertEquals("Edsger Dijkstra", input("Name").getValue());
    assertEquals("edsger@goto.dev", input("Email").getValue());
    assertEquals(Status.ACTIVE, ComponentLocator.of(Ui5Select.class).locate().getValue());
  }

  @Test
  void shouldSaveWhatTheUserChanged(WebforjTestbed context) {
    open(context, String.valueOf(id));

    type(input("Name"), "E. W. Dijkstra");
    // The select reports the position of the chosen option, and Paused is the third one.
    UserInteraction.fireEvent(ComponentLocator.of(Ui5Select.class).locate(), "change",
        Map.of("value", "2"));
    UserInteraction.click(button("Save"));

    Customer saved = service.find(id).orElseThrow();
    assertEquals("E. W. Dijkstra", saved.getName());
    assertEquals(Status.PAUSED, saved.getStatus());
    assertEquals("Saved E. W. Dijkstra", toast().getText());
    assertTrue(ComponentLocator.of(CustomersView.class).isPresent());
  }

  @Test
  void shouldShowAValidationErrorOnTheField(WebforjTestbed context) {
    open(context, String.valueOf(id));

    type(input("Email"), "not-an-email");
    UserInteraction.click(button("Save"));

    // The constraint on Customer.email failed, so nothing is saved and the field shows why.
    assertTrue(input("Email").isInvalid());
    assertEquals("Enter a valid email address", input("Email").getInvalidMessage());
    assertEquals("edsger@goto.dev", service.find(id).orElseThrow().getEmail());
  }

  @Test
  void shouldRejectTextThatIsNoDate(WebforjTestbed context) {
    open(context, String.valueOf(id));
    Ui5DatePicker picker = ComponentLocator.of(Ui5DatePicker.class).locate();

    UserInteraction.fireEvent(picker, "change", Map.of("value", "not a date"));
    UserInteraction.click(button("Save"));

    // The text reads as no date, and saving it would erase the contract start.
    assertTrue(picker.isInvalid());
    assertEquals("Enter a valid date", picker.getInvalidMessage());
    assertEquals(LocalDate.of(2024, 4, 2), service.find(id).orElseThrow().getContractStart());
  }

  @Test
  void shouldCreateACustomer(WebforjTestbed context) {
    open(context, CustomerFormView.NEW);
    assertFalse(button("Delete").isEnabled());

    type(input("Name"), "Frances Allen");
    type(input("Email"), "frances@ibm.dev");
    UserInteraction.click(button("Save"));

    assertEquals(1, service.search("frances@ibm.dev").size());
  }

  @Test
  void shouldDeleteOnlyAfterTheUserConfirms(WebforjTestbed context) {
    open(context, String.valueOf(id));

    UserInteraction.click(button("Delete"));
    assertTrue(ComponentLocator.of(Ui5Dialog.class).locate().isOpened());
    assertTrue(service.find(id).isPresent());

    UserInteraction.click(button("Yes, delete"));
    assertTrue(service.find(id).isEmpty());
    assertEquals("Deleted Edsger Dijkstra", toast().getText());
  }

  @Test
  void shouldGoBackToTheListForAnUnknownCustomer(WebforjTestbed context) {
    open(context, "999999");

    assertTrue(ComponentLocator.of(CustomersView.class).isPresent());
  }

  @Test
  void shouldShowTheListAgainWhenTheCustomerOfAClickedRowIsGone(WebforjTestbed context) {
    context.navigate(CustomersView.class);

    // Someone else deleted the customer while the list still showed its row.
    open(context, "999999");

    assertTrue(ComponentLocator.of(CustomersView.class).isPresent());
  }

  @Test
  void shouldGoBackToTheListForAnIdThatIsNoNumber(WebforjTestbed context) {
    open(context, "abc");
    assertTrue(ComponentLocator.of(CustomersView.class).isPresent());

    open(context, "1abc");
    assertTrue(ComponentLocator.of(CustomersView.class).isPresent());
  }

  private static void open(WebforjTestbed context, String customerId) {
    context.getRouter().orElseThrow().navigate(CustomerFormView.class,
        ParametersBag.of("id=" + customerId));
  }

  // The input reports every keystroke with an input event that carries its value.
  private static void type(Ui5Input input, String text) {
    UserInteraction.fireEvent(input, "input", Map.of("value", text));
  }

  private static Ui5Input input(String label) {
    return ComponentLocator.of(Ui5Input.class).byLabel(label).locate();
  }

  private static Ui5Button button(String text) {
    return ComponentLocator.of(Ui5Button.class).byText(text).locate();
  }

  private static Ui5Toast toast() {
    return ComponentLocator.of(Ui5Toast.class).locate();
  }
}
