package com.webforj.builtwithwebforj.springsecurity.components;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.concern.HasStyle;
/**
 * Reusable page header component with title, subtitle, and optional action components.
 */
public class PageHeader extends Composite<FlexLayout> {

  private final FlexLayout titleSection;
  private final H1 titleElement;
  private Paragraph subtitleElement;

  /**
   * Creates a page header with just a title.
   *
   * @param title the page title
   */
  public PageHeader(String title) {
    this(title, null);
  }

  /**
   * Creates a page header with a title and subtitle.
   *
   * @param title the page title
   * @param subtitle the page subtitle (can be null)
   */
  public PageHeader(String title, String subtitle) {
    FlexLayout header = getBoundComponent();
    header.setJustifyContent(FlexJustifyContent.BETWEEN);
    header.setAlignment(FlexAlignment.CENTER);
    header.addClassName("page-header");

    // Title section
    titleSection = new FlexLayout();
    titleSection.setDirection(FlexDirection.COLUMN);

    titleElement = new H1(title);
    titleElement.addClassName("page-title");
    titleSection.add(titleElement);

    if (subtitle != null && !subtitle.isEmpty()) {
      subtitleElement = new Paragraph(subtitle);
      subtitleElement.addClassName("page-subtitle");
      titleSection.add(subtitleElement);
    }

    header.add(titleSection);
  }

  /**
   * Creates a page header with title, subtitle, and action components.
   *
   * @param title the page title
   * @param subtitle the page subtitle (can be null)
   * @param actions components to display on the right side (buttons, dropdowns, etc.)
   */
  public PageHeader(String title, String subtitle, Component... actions) {
    this(title, subtitle);

    if (actions != null && actions.length > 0) {
      for (Component action : actions) {
        getBoundComponent().add(action);
      }
    }
  }

  /**
   * Adds a component above the title (e.g., ticket number, breadcrumb).
   *
   * @param component the component to add above the title
   * @return this PageHeader for method chaining
   */
  public <T extends Component & HasStyle<T>> PageHeader withPrefixContent(T component) {
    // Insert at the beginning of title section, before the H1
    titleSection.add(component);
    titleSection.setItemOrder(0, component);
    return this;
  }

  /**
   * Updates the title text.
   *
   * @param title the new title
   */
  public void setTitle(String title) {
    titleElement.setText(title);
  }

  /**
   * Sets the vertical alignment of header items.
   * Use START for headers where content may wrap to multiple lines.
   *
   * @param alignment the alignment to use
   * @return this PageHeader for method chaining
   */
  public PageHeader withAlignment(FlexAlignment alignment) {
    getBoundComponent().setAlignment(alignment);
    return this;
  }
}
