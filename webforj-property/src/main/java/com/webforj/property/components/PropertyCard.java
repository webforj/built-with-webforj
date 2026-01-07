package com.webforj.property.components;

import com.webforj.component.Composite;
import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.property.model.Property;
import com.webforj.property.model.PropertyType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Property card component displaying a property listing summary.
 *
 * <p>Shows key property information in a compact text-based format: price, type badge, address, and
 * stats. Images are shown only in the detail view when a property is selected.
 */
public class PropertyCard extends Composite<Div> {

  private final Div self = getBoundComponent();
  private final Property property;
  private final List<Consumer<Property>> clickListeners = new ArrayList<>();

  /**
   * Creates a property card for the given property.
   *
   * @param property the property to display
   */
  public PropertyCard(Property property) {
    this.property = property;
    self.addClassName("property-card");
    self.onClick(this::handleClick);
    buildCard();
  }

  private void handleClick(ElementClickEvent<Div> event) {
    clickListeners.forEach(listener -> listener.accept(property));
  }

  private void buildCard() {
    Div street = new Div();
    street.addClassName("property-card__street");
    street.setText(property.street());

    Div location = new Div();
    location.addClassName("property-card__location");
    location.setText(String.format("%s, %s %s", property.city(), property.state(), property.zip()));

    self.add(createHeader(), street, location, createStats(), createFooter(), createStatus());
  }

  private FlexLayout createHeader() {
    Span price = new Span(property.getFormattedPrice());
    price.addClassName("property-card__price");

    Span badge = new Span(property.type().getDisplayName());
    badge.addClassName("property-card__badge");
    badge.addClassName("property-card__badge--" + property.type().getIconId());

    FlexLayout header =
        FlexLayout.create(price, badge)
            .horizontal()
            .align()
            .center()
            .justify()
            .between()
            .build()
            .setSpacing("var(--dwc-space-s)");
    header.addClassName("property-card__header");
    return header;
  }

  private FlexLayout createStats() {
    FlexLayout stats = FlexLayout.create().horizontal().build().setSpacing("var(--dwc-space-m)");
    stats.addClassName("property-card__stats");

    if (property.type() != PropertyType.COMMERCIAL) {
      Span beds = new Span(property.beds() + " bd");
      beds.addClassName("property-card__stat");

      Span baths = new Span(property.baths() + " ba");
      baths.addClassName("property-card__stat");

      stats.add(beds, baths);
    }

    Span sqft = new Span(String.format("%,d sqft", property.sqft()));
    sqft.addClassName("property-card__stat");
    stats.add(sqft);

    return stats;
  }

  private Div createFooter() {
    Div footer = new Div();
    footer.addClassName("property-card__footer");

    Span year = new Span("Built " + property.yearBuilt());
    year.addClassName("property-card__meta");

    Span pricePerSqft = new Span(property.getFormattedPricePerSqft());
    pricePerSqft.addClassName("property-card__meta");

    footer.add(year, pricePerSqft);
    return footer;
  }

  private Div createStatus() {
    Div status = new Div();
    status.addClassName("property-card__status");
    status.addClassName("property-card__status--" + property.status().name().toLowerCase());
    status.setText(property.status().getDisplayName());
    return status;
  }

  /**
   * Gets the property associated with this card.
   *
   * @return the property
   */
  public Property getProperty() {
    return property;
  }

  /**
   * Adds a click listener that receives the property when clicked.
   *
   * @param listener the listener to call with the property
   */
  public void addClickListener(Consumer<Property> listener) {
    clickListeners.add(listener);
  }

  /**
   * Sets the selected state of this card.
   *
   * @param selected true to mark as selected, false to deselect
   */
  public void setSelected(boolean selected) {
    if (selected) {
      self.addClassName("property-card--selected");
    } else {
      self.removeClassName("property-card--selected");
    }
  }

  /**
   * Sets the visibility of this card.
   *
   * @param visible true to show, false to hide
   */
  public void setCardVisible(boolean visible) {
    self.setStyle("display", visible ? "block" : "none");
  }
}
