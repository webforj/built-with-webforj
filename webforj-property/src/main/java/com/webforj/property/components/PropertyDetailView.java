package com.webforj.property.components;

import com.webforj.component.Composite;
import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.property.model.Property;
import com.webforj.property.model.PropertyType;
import java.util.ArrayList;
import java.util.List;

/**
 * Property detail view showing full information about a selected property.
 *
 * <p>Displays large property image, complete address, price with price-per-sqft, all property
 * specs, description text, and status/type badges. Includes a back button to return to the listing
 * view.
 */
public class PropertyDetailView extends Composite<Div> {

  private final Div self = getBoundComponent();
  private final List<Runnable> backListeners = new ArrayList<>();
  private final Property property;

  /**
   * Creates the detail view for a property.
   *
   * @param property the property to display
   */
  public PropertyDetailView(Property property) {
    this.property = property;
    self.addClassName("property-detail");
    buildView();
  }

  private void buildView() {
    Div content = new Div();
    content.addClassName("property-detail__content");
    content.add(
        createPriceSection(), createAddressSection(), createStatsGrid(), createDescription());

    self.add(createBackButton(), createImageSection(), content);
  }

  private Div createBackButton() {
    Div backButton = new Div();
    backButton.addClassName("property-detail__back");
    backButton.setText("← Back to listings");
    backButton.onClick(this::handleBackClick);
    return backButton;
  }

  private Div createImageSection() {
    Div image = new Div();
    image.addClassName("property-detail__image");
    image.setStyle("background-image", "url('" + property.imageUrl() + "')");

    Div statusBadge = new Div();
    statusBadge.addClassName("property-detail__status");
    statusBadge.addClassName("property-detail__status--" + property.status().name().toLowerCase());
    statusBadge.setText(property.status().getDisplayName());

    image.add(statusBadge);
    return image;
  }

  private Div createPriceSection() {
    Div priceSection = new Div();
    priceSection.addClassName("property-detail__price-section");

    Span price = new Span(property.getFormattedPrice());
    price.addClassName("property-detail__price");

    Span pricePerSqft = new Span(property.getFormattedPricePerSqft());
    pricePerSqft.addClassName("property-detail__price-sqft");

    Span typeBadge = new Span(property.type().getDisplayName());
    typeBadge.addClassName("property-detail__type-badge");
    typeBadge.addClassName("property-detail__type-badge--" + property.type().getIconId());

    priceSection.add(price, pricePerSqft, typeBadge);
    return priceSection;
  }

  private Div createAddressSection() {
    Div addressSection = new Div();
    addressSection.addClassName("property-detail__address-section");

    Div street = new Div();
    street.addClassName("property-detail__street");
    street.setText(property.street());

    Div location = new Div();
    location.addClassName("property-detail__location");
    location.setText(String.format("%s, %s %s", property.city(), property.state(), property.zip()));

    addressSection.add(street, location);
    return addressSection;
  }

  private Div createStatsGrid() {
    Div statsGrid = new Div();
    statsGrid.addClassName("property-detail__stats-grid");

    if (property.type() != PropertyType.COMMERCIAL) {
      statsGrid.add(createStatItem("Bedrooms", String.valueOf(property.beds())));
      statsGrid.add(createStatItem("Bathrooms", String.valueOf(property.baths())));
    }
    statsGrid.add(createStatItem("Square Feet", String.format("%,d", property.sqft())));
    if (property.lotSize() != null) {
      statsGrid.add(createStatItem("Lot Size", String.format("%,d sqft", property.lotSize())));
    }
    statsGrid.add(createStatItem("Year Built", String.valueOf(property.yearBuilt())));

    return statsGrid;
  }

  private Div createDescription() {
    Div descriptionSection = new Div();
    descriptionSection.addClassName("property-detail__description-section");

    Div descriptionLabel = new Div();
    descriptionLabel.addClassName("property-detail__description-label");
    descriptionLabel.setText("Description");

    Paragraph description = new Paragraph();
    description.addClassName("property-detail__description");
    description.setText(property.description());

    descriptionSection.add(descriptionLabel, description);
    return descriptionSection;
  }

  private void handleBackClick(ElementClickEvent<?> event) {
    backListeners.forEach(Runnable::run);
  }

  /** Creates a stat item with label and value. */
  private Div createStatItem(String label, String value) {
    Div item = new Div();
    item.addClassName("property-detail__stat-item");

    Div itemValue = new Div();
    itemValue.addClassName("property-detail__stat-value");
    itemValue.setText(value);

    Div itemLabel = new Div();
    itemLabel.addClassName("property-detail__stat-label");
    itemLabel.setText(label);

    item.add(itemValue, itemLabel);
    return item;
  }

  /**
   * Adds a listener for back button clicks.
   *
   * @param listener the listener to call when back is clicked
   */
  public void addBackListener(Runnable listener) {
    backListeners.add(listener);
  }
}
