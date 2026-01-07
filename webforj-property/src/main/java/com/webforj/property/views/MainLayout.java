package com.webforj.property.views;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.property.components.FilterBar;
import com.webforj.property.components.MapPanel;
import com.webforj.property.components.Sidebar;
import com.webforj.property.data.PropertyDataProvider;
import com.webforj.property.model.FilterState;
import com.webforj.property.model.Property;
import com.webforj.router.annotation.Route;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Main application layout with filter bar, sidebar, and map panel. */
@Route
public class MainLayout extends Composite<FlexLayout> {

  private final FlexLayout self = getBoundComponent();
  private final FilterBar filterBar;
  private final Sidebar sidebar;
  private final MapPanel mapPanel;
  private List<Property> properties;
  private Map<String, Property> propertyMap = new HashMap<>();
  private String selectedPropertyId;

  /** Creates the main layout with filter bar, sidebar, and map panel. */
  public MainLayout() {
    self.setDirection(FlexDirection.COLUMN);
    self.setSpacing("0px");
    self.addClassName("property-layout");

    filterBar = new FilterBar();
    sidebar = new Sidebar();
    mapPanel = new MapPanel();

    FlexLayout contentArea = FlexLayout.create(sidebar, mapPanel).horizontal().build();
    contentArea.addClassName("content-area");

    self.add(filterBar, contentArea);

    setupInteractions();
    loadProperties();
  }

  /** Sets up interaction handlers for sidebar, map, and filter bar. */
  private void setupInteractions() {
    sidebar.addPropertySelectListener(this::handlePropertySelect);
    sidebar.addBackListener(this::handleSidebarBack);
    mapPanel.addFeatureClickListener(this::handleMapFeatureClick);
    mapPanel.addMapClickListener(this::handleMapBackgroundClick);
    filterBar.addFilterChangeListener(this::applyFilters);
  }

  private void handlePropertySelect(Property property) {
    selectProperty(property.id());
    sidebar.showDetailView(property);
    filterBar.setFiltersEnabled(false);
  }

  private void handleSidebarBack() {
    deselectProperty();
    sidebar.showListView();
    filterBar.setFiltersEnabled(true);
  }

  private void handleMapFeatureClick(String propertyId) {
    Property property = propertyMap.get(propertyId);
    if (property != null) {
      selectProperty(propertyId);
      sidebar.showDetailView(property);
      filterBar.setFiltersEnabled(false);
    }
  }

  private void handleMapBackgroundClick() {
    if (selectedPropertyId != null && sidebar.getSelectedPropertyId() != null) {
      deselectProperty();
    }
  }

  /**
   * Applies the current filter state to the sidebar and map.
   *
   * @param filterState the filter state to apply
   */
  private void applyFilters(FilterState filterState) {
    Set<String> visibleIds = sidebar.applyFilter(filterState);
    mapPanel.setMarkerVisibility(visibleIds);

    if (selectedPropertyId != null && !visibleIds.contains(selectedPropertyId)) {
      deselectProperty();
      sidebar.showListView();
    }
  }

  /**
   * Selects a property, highlighting it on both sidebar and map.
   *
   * @param propertyId the property to select
   */
  private void selectProperty(String propertyId) {
    selectedPropertyId = propertyId;
    sidebar.setSelectedProperty(propertyId);
    mapPanel.selectProperty(propertyId);
    mapPanel.centerOnProperty(propertyId);
  }

  /** Deselects the current property. */
  private void deselectProperty() {
    selectedPropertyId = null;
    sidebar.setSelectedProperty(null);
    mapPanel.deselectProperty();
  }

  /** Loads properties and populates the sidebar and map. */
  private void loadProperties() {
    properties = PropertyDataProvider.getProperties();

    for (Property property : properties) {
      propertyMap.put(property.id(), property);
    }

    sidebar.setProperties(properties);

    for (Property property : properties) {
      mapPanel.addPropertyMarker(
          property.id(),
          property.latitude(),
          property.longitude(),
          property.type().name().toLowerCase());
    }
  }
}
