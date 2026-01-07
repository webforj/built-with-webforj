package com.webforj.property.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.property.model.FilterState;
import com.webforj.property.model.Property;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Sidebar containing property listings.
 *
 * <p>Fixed width with a header showing property count and a scrollable list of property cards.
 */
public class Sidebar extends Composite<FlexLayout> {

  private final FlexLayout self = getBoundComponent();
  private final FlexLayout header;
  private final Span countLabel;
  private final FlexLayout listContainer;
  private final Div emptyState;
  private final List<PropertyCard> propertyCards = new ArrayList<>();
  private final Map<String, PropertyCard> cardMap = new HashMap<>();

  private PropertyDetailView detailView;
  private String selectedPropertyId;
  private final List<Consumer<Property>> propertySelectListeners = new ArrayList<>();
  private final List<Runnable> backListeners = new ArrayList<>();

  /** Creates the sidebar container. */
  public Sidebar() {
    self.setDirection(FlexDirection.COLUMN);
    self.addClassName("sidebar");

    countLabel = new Span("0 properties");
    countLabel.addClassName("sidebar__count");

    header = FlexLayout.create(countLabel).horizontal().align().center().build();
    header.addClassName("sidebar__header");

    listContainer = FlexLayout.create().vertical().build();
    listContainer.setSpacing("var(--dwc-space-m)");
    listContainer.addClassName("sidebar__list");

    emptyState = createEmptyState();

    self.add(header, listContainer, emptyState);
  }

  private Div createEmptyState() {
    Div state = new Div();
    state.addClassName("sidebar__empty-state");

    Div emptyIcon = new Div();
    emptyIcon.addClassName("sidebar__empty-icon");
    emptyIcon.setText("🏠");

    Span emptyTitle = new Span("No properties found");
    emptyTitle.addClassName("sidebar__empty-title");

    Span emptyMessage = new Span("Try adjusting your filters to see more results");
    emptyMessage.addClassName("sidebar__empty-message");

    state.add(emptyIcon, emptyTitle, emptyMessage);
    state.setVisible(false);
    return state;
  }

  /**
   * Sets the properties to display in the sidebar.
   *
   * @param properties the list of properties
   */
  public void setProperties(List<Property> properties) {
    listContainer.removeAll();
    propertyCards.clear();
    cardMap.clear();

    for (Property property : properties) {
      PropertyCard card = new PropertyCard(property);
      propertyCards.add(card);
      cardMap.put(property.id(), card);
      listContainer.add(card);
      card.addClickListener(this::handlePropertyCardClick);
    }

    updateCount(properties.size());
  }

  private void handlePropertyCardClick(Property property) {
    propertySelectListeners.forEach(listener -> listener.accept(property));
  }

  /**
   * Updates the property count label.
   *
   * @param count the number of properties
   */
  private void updateCount(int count) {
    String text = count == 1 ? "1 property" : count + " properties";
    countLabel.setText(text);
  }

  /**
   * Adds a listener for property selection.
   *
   * @param listener the listener to call when a property is selected
   */
  public void addPropertySelectListener(Consumer<Property> listener) {
    propertySelectListeners.add(listener);
  }

  /**
   * Adds a listener for back button clicks in detail view.
   *
   * @param listener the listener to call when back is clicked
   */
  public void addBackListener(Runnable listener) {
    backListeners.add(listener);
  }

  /**
   * Shows the detail view for a property.
   *
   * @param property the property to display
   */
  public void showDetailView(Property property) {
    header.setVisible(false);
    listContainer.setVisible(false);

    if (detailView != null) {
      self.remove(detailView);
    }

    detailView = new PropertyDetailView(property);
    detailView.addBackListener(this::handleDetailViewBack);
    self.add(detailView);
  }

  private void handleDetailViewBack() {
    backListeners.forEach(Runnable::run);
  }

  /** Returns to the list view from detail view. */
  public void showListView() {
    if (detailView != null) {
      self.remove(detailView);
      detailView = null;
    }

    header.setVisible(true);
    listContainer.setVisible(true);
  }

  /**
   * Sets the selected property by ID, highlighting its card.
   *
   * @param propertyId the property ID to select, or null to deselect
   */
  public void setSelectedProperty(String propertyId) {
    if (selectedPropertyId != null) {
      PropertyCard previousCard = cardMap.get(selectedPropertyId);
      if (previousCard != null) {
        previousCard.setSelected(false);
      }
    }

    selectedPropertyId = propertyId;
    if (propertyId != null) {
      PropertyCard card = cardMap.get(propertyId);
      if (card != null) {
        card.setSelected(true);
      }
    }
  }

  /**
   * Gets the currently selected property ID.
   *
   * @return the selected property ID, or null if none selected
   */
  public String getSelectedPropertyId() {
    return selectedPropertyId;
  }

  /**
   * Applies a filter to the property cards, showing only matching properties.
   *
   * @param filterState the filter state to apply
   * @return the set of property IDs that are visible after filtering
   */
  public Set<String> applyFilter(FilterState filterState) {
    Set<String> visibleIds = new HashSet<>();

    for (PropertyCard card : propertyCards) {
      Property property = card.getProperty();
      boolean matches = filterState.matches(property);
      card.setCardVisible(matches);

      if (matches) {
        visibleIds.add(property.id());
      }
    }

    updateCount(visibleIds.size());

    boolean hasResults = !visibleIds.isEmpty();
    listContainer.setVisible(hasResults);
    emptyState.setVisible(!hasResults);

    return visibleIds;
  }

  /**
   * Gets the property by ID.
   *
   * @param propertyId the property ID
   * @return the property, or null if not found
   */
  public Property getPropertyById(String propertyId) {
    PropertyCard card = cardMap.get(propertyId);
    return card != null ? card.getProperty() : null;
  }
}
