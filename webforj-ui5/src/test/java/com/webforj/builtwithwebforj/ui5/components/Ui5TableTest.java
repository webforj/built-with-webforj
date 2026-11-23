package com.webforj.builtwithwebforj.ui5.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.component.element.Element;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

// Tests the table on its own, without the app.
@WebforjTest
class Ui5TableTest {

  @Test
  void shouldKeepColumnsWithTheSameHeader(WebforjTestbed context) {
    Ui5Table<String> table = new Ui5Table<>();
    context.add(table);

    table.addColumn("Text", String::toUpperCase).addColumn("Text", String::toLowerCase);
    table.setItems(List.of("Ada"));

    assertEquals(List.of("ADA", "ada"), cells(table));
  }

  @Test
  void shouldIgnoreAClickOnARowThatIsGone(WebforjTestbed context) {
    Ui5Table<String> table = new Ui5Table<>();
    context.add(table);
    List<String> clicked = new ArrayList<>();
    table.onItemClick(clicked::add);
    table.setItems(List.of("Ada", "Alan"));
    table.setItems(List.of("Ada"));

    // The browser can still show the second row when the click on it is sent.
    UserInteraction.fireEvent(table, "row-click", Map.of("rowKey", "1"));
    assertTrue(clicked.isEmpty());

    UserInteraction.fireEvent(table, "row-click", Map.of("rowKey", "0"));
    assertEquals(List.of("Ada"), clicked);
  }

  private static List<String> cells(Ui5Table<?> table) {
    return ComponentLocator.of(Element.class).within(table)
        .filter(element -> "ui5-table-cell".equals(element.getNodeName())).all().stream()
        .map(Element::getText).toList();
  }
}
