package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.component.button.Button;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;

/**
 * Reusable empty state component for when lists or tables have no data.
 */
public class EmptyState extends Composite<Div> {

  /**
   * Creates an empty state with icon, title, and message.
   *
   * @param icon the TablerIcon name (e.g., "ticket-off")
   * @param title the title text
   * @param message the description message
   */
  public EmptyState(String icon, String title, String message) {
    this(icon, title, message, null);
  }

  /**
   * Creates an empty state with icon, title, message, and optional action button.
   *
   * @param icon the TablerIcon name (e.g., "ticket-off")
   * @param title the title text
   * @param message the description message
   * @param actionButton optional button for the user to take action (can be null)
   */
  public EmptyState(String icon, String title, String message, Button actionButton) {
    Div container = getBoundComponent();
    container.addClassName("empty-state");

    // Icon wrapper
    Div iconWrapper = new Div();
    iconWrapper.addClassName("empty-state-icon");
    iconWrapper.add(TablerIcon.create(icon));
    container.add(iconWrapper);

    // Title
    H1 titleElement = new H1(title);
    titleElement.addClassName("empty-state-title");
    container.add(titleElement);

    // Message
    Paragraph messageElement = new Paragraph(message);
    messageElement.addClassName("empty-state-message");
    container.add(messageElement);

    // Optional action button
    if (actionButton != null) {
      container.add(actionButton);
    }
  }
}
