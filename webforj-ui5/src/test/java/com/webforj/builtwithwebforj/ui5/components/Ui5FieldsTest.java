package com.webforj.builtwithwebforj.ui5.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

// Tests the form controls on their own, without the app. A browser event is fired by its name
// with the element property it reports, which is what the browser sends to the server.
@WebforjTest
class Ui5FieldsTest {

  @Test
  void inputShouldReportWhatTheUserTypes(WebforjTestbed context) {
    Ui5Input input = new Ui5Input("Name");
    context.add(input);
    List<String> changes = new ArrayList<>();
    input.onValueChange(e -> changes.add(e.getValue()));

    UserInteraction.fireEvent(input, "input", Map.of("value", "Ada"));

    assertEquals("Ada", input.getValue());
    assertEquals(List.of("Ada"), changes);
  }

  @Test
  void inputShouldShowAnErrorTheUi5Way(WebforjTestbed context) {
    Ui5Input input = new Ui5Input("Name");
    context.add(input);

    input.setInvalid(true).setInvalidMessage("Name is required");

    assertTrue(input.isInvalid());
    assertEquals("Name is required", input.getInvalidMessage());
  }

  @Test
  void inputShouldSupportTheWebforjConcerns(WebforjTestbed context) {
    Ui5Input input = new Ui5Input("Name");
    context.add(input);

    // The same setters every built-in webforJ field has, mapped to the UI5 properties.
    input.setPlaceholder("Full name").setRequired(true).setReadOnly(true).setVisible(false);

    assertEquals("Name", input.getLabel());
    assertEquals("Full name", input.getPlaceholder());
    assertTrue(input.isRequired());
    assertTrue(input.isReadOnly());
    assertFalse(input.isVisible());
  }

  @Test
  void selectShouldTurnThePositionIntoTheItem(WebforjTestbed context) {
    Ui5Select<String> select = new Ui5Select<>("Size");
    context.add(select);
    select.setItems(List.of("Small", "Medium", "Large"), String::toUpperCase);
    assertEquals("Small", select.getValue());

    select.setValue("Large");
    assertEquals("Large", select.getValue());

    UserInteraction.fireEvent(select, "change", Map.of("value", "1"));
    assertEquals("Medium", select.getValue());
  }

  @Test
  void selectShouldSelectTheFirstOfNewItems(WebforjTestbed context) {
    Ui5Select<String> select = new Ui5Select<>("Size");
    context.add(select);
    select.setItems(List.of("Small", "Medium", "Large"), String::toUpperCase);
    select.setValue("Large");

    select.setItems(List.of("Short", "Long"), String::toUpperCase);

    assertEquals("Short", select.getValue());
  }

  @Test
  void datePickerShouldExchangeIsoDates(WebforjTestbed context) {
    Ui5DatePicker picker = new Ui5DatePicker("Start");
    context.add(picker);
    assertNull(picker.getValue());

    picker.setValue(LocalDate.of(2025, 1, 31));
    assertEquals(LocalDate.of(2025, 1, 31), picker.getValue());

    UserInteraction.fireEvent(picker, "change", Map.of("value", "2024-02-29"));
    assertEquals(LocalDate.of(2024, 2, 29), picker.getValue());
    assertFalse(picker.hasInvalidText());

    UserInteraction.fireEvent(picker, "change", Map.of("value", "not a date"));
    assertNull(picker.getValue());
    assertTrue(picker.hasInvalidText());

    UserInteraction.fireEvent(picker, "change", Map.of("value", ""));
    assertNull(picker.getValue());
    assertFalse(picker.hasInvalidText());
  }

  @Test
  void switchShouldHoldABoolean(WebforjTestbed context) {
    Ui5Switch toggle = new Ui5Switch("Newsletter");
    context.add(toggle);
    assertFalse(toggle.getValue());

    UserInteraction.fireEvent(toggle, "change", Map.of("checked", true));

    assertTrue(toggle.getValue());
  }
}
