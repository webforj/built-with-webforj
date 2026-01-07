package com.webforj.property.components;

import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;
import java.util.function.Consumer;

/**
 * A removable filter chip component.
 *
 * <p>Displays a label with a remove button. Used to show active filters that can be dismissed by
 * the user.
 */
class FilterChip {

  private final Div container;
  private final Span label;

  /**
   * Creates a new filter chip.
   *
   * @param onRemove callback invoked when the remove button is clicked
   */
  FilterChip(Consumer<ElementClickEvent<?>> onRemove) {
    container = new Div();
    container.addClassName("filter-bar__chip");
    container.setStyle("display", "none");

    label = new Span();
    label.addClassName("filter-bar__chip-label");

    Div removeBtn = new Div();
    removeBtn.addClassName("filter-bar__chip-remove");
    removeBtn.setText("✕");
    removeBtn.onClick(onRemove::accept);

    container.add(label, removeBtn);
  }

  /**
   * Shows the chip with the specified text.
   *
   * @param text the label text to display
   */
  void show(String text) {
    label.setText(text);
    container.setStyle("display", "flex");
  }

  /** Hides the chip. */
  void hide() {
    container.setStyle("display", "none");
  }

  /**
   * Gets the container element for adding to a parent.
   *
   * @return the chip container
   */
  Div getContainer() {
    return container;
  }
}
