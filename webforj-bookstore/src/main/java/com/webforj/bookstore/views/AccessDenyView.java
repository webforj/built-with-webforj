package com.webforj.bookstore.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.Route;

/**
 * View displayed when a user attempts to access a protected resource without
 * sufficient permissions.
 *
 */
@Route(value = "/access-denied", outlet = MainLayout.class)
@StyleSheet("ws://access-deny.css")
public class AccessDenyView extends Composite<FlexLayout> {
  private final FlexLayout self = getBoundComponent();

  /**
   * Constructs the AccessDenyView with an icon, title, and message in a card.
   */
  public AccessDenyView() {
    self.setDirection(FlexDirection.COLUMN);
    self.setAlignment(FlexAlignment.CENTER);
    self.setJustifyContent(FlexJustifyContent.CENTER);
    self.setHeight("100%");
    self.addClassName("access-deny-view");

    Icon icon = TablerIcon.create("shield-lock");
    icon.addClassName("access-deny-view__icon");

    H1 title = new H1("VIP Only");
    title.addClassName("access-deny-view__title");

    Paragraph message = new Paragraph(
        "Looks like you tried to sneak into the executive lounge! "
            + "Either grab better credentials or head back to the public areas where the coffee is free anyway.");
    message.addClassName("access-deny-view__message");

    Div card = new Div();
    card.addClassName("access-deny-view__card");
    card.add(icon, title, message);

    self.add(card);
  }
}
