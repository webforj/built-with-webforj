package com.webforj.property.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.map.Coordinate;
import com.webforj.component.map.Extent;
import com.webforj.component.map.Map;
import com.webforj.component.map.event.FeatureClickEvent;
import com.webforj.component.map.event.MapClickEvent;
import com.webforj.component.map.feature.MarkerFeature;
import com.webforj.component.map.style.IconStyle;
import com.webforj.component.map.style.Style;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Map panel containing the interactive OpenLayers map.
 *
 * <p>Displays an interactive map centered on Austin, TX with OpenStreetMap tiles and a vector layer
 * for property markers. Provides methods to add/remove property markers with custom icons based on
 * property type.
 */
public class MapPanel extends Composite<Div> {

  private static final double AUSTIN_LON = -97.7431;
  private static final double AUSTIN_LAT = 30.2672;
  private static final double DEFAULT_ZOOM = 11.0;
  private static final double MIN_ZOOM = 10.0;
  private static final double MAX_ZOOM = 18.0;
  private static final Extent AUSTIN_EXTENT = new Extent(30.0, -98.2, 30.6, -97.3);

  private final Div self = getBoundComponent();
  private final Map map;
  private final java.util.Map<String, PropertyMarkerData> propertyData = new HashMap<>();

  private String selectedPropertyId;
  private final List<Consumer<String>> featureClickListeners = new ArrayList<>();
  private final List<Runnable> mapClickListeners = new ArrayList<>();

  /** Creates the map panel with an interactive map. */
  public MapPanel() {
    self.addClassName("map-panel");

    map = new Map();
    initializeMap();
    setupEventListeners();

    self.add(map);
  }

  /** Sets up map event listeners for feature and map clicks. */
  private void setupEventListeners() {
    map.addFeatureClickListener(this::handleFeatureClick);
    map.addMapClickListener(this::handleMapClick);
  }

  private void handleFeatureClick(FeatureClickEvent event) {
    String featureId = event.getFeatureId();
    String propertyId = findPropertyIdByMarkerId(featureId);
    if (propertyId != null) {
      featureClickListeners.forEach(listener -> listener.accept(propertyId));
    }
  }

  private String findPropertyIdByMarkerId(String markerId) {
    for (var entry : propertyData.entrySet()) {
      PropertyMarkerData data = entry.getValue();
      if (data.isVisible() && data.getActiveMarker().getId().equals(markerId)) {
        return entry.getKey();
      }
    }
    return null;
  }

  private void handleMapClick(MapClickEvent event) {
    mapClickListeners.forEach(Runnable::run);
  }

  /** Configures the map view settings. */
  private void initializeMap() {
    map.setCenter(AUSTIN_LAT, AUSTIN_LON);
    map.setZoom(DEFAULT_ZOOM);
    map.setMinZoom(MIN_ZOOM);
    map.setMaxZoom(MAX_ZOOM);
    map.setExtent(AUSTIN_EXTENT);
  }

  /**
   * Adds a property marker to the map.
   *
   * @param id the unique property ID
   * @param lat the latitude coordinate
   * @param lon the longitude coordinate
   * @param propertyType the property type
   */
  public void addPropertyMarker(String id, double lat, double lon, String propertyType) {
    Coordinate coordinate = new Coordinate(lat, lon);
    Style markerStyle = createMarkerStyle(propertyType, false);

    MarkerFeature marker = MarkerFeature.create().coordinate(coordinate).style(markerStyle).build();

    map.addMarker(marker);
    PropertyMarkerData data = new PropertyMarkerData(coordinate, propertyType);
    data.setActiveMarker(marker);
    propertyData.put(id, data);
  }

  /**
   * Creates the marker style for a given property type.
   *
   * @param propertyType the property type
   * @param selected whether the marker is selected
   * @return the configured style
   */
  private Style createMarkerStyle(String propertyType, boolean selected) {
    String iconName = "marker-" + propertyType.toLowerCase() + ".svg";
    String iconUrl = "icons://" + iconName;
    double scale = selected ? 1.4 : 1.0;

    IconStyle iconStyle = IconStyle.create().src(iconUrl).scale(scale).anchor(0.5, 1.0).build();

    return Style.create().icon(iconStyle).build();
  }

  /**
   * Selects a property marker, highlighting it and deselecting the previous selection.
   *
   * @param propertyId the property ID to select
   */
  public void selectProperty(String propertyId) {
    if (selectedPropertyId != null && !selectedPropertyId.equals(propertyId)) {
      updateMarkerStyle(selectedPropertyId, false);
    }

    selectedPropertyId = propertyId;
    if (propertyId != null) {
      updateMarkerStyle(propertyId, true);
    }
  }

  /** Deselects the currently selected property marker. */
  public void deselectProperty() {
    if (selectedPropertyId != null) {
      updateMarkerStyle(selectedPropertyId, false);
      selectedPropertyId = null;
    }
  }

  /** Updates a marker's style by removing and re-adding it. */
  private void updateMarkerStyle(String propertyId, boolean selected) {
    PropertyMarkerData data = propertyData.get(propertyId);
    if (data == null || !data.isVisible()) {
      return;
    }

    map.removeFeature(data.getActiveMarker());

    Style newStyle = createMarkerStyle(data.getPropertyType(), selected);
    MarkerFeature newMarker =
        MarkerFeature.create().coordinate(data.getCoordinate()).style(newStyle).build();

    map.addMarker(newMarker);
    data.setActiveMarker(newMarker);
  }

  /**
   * Centers the map on a property.
   *
   * @param propertyId the property ID to center on
   */
  public void centerOnProperty(String propertyId) {
    PropertyMarkerData data = propertyData.get(propertyId);
    if (data != null) {
      map.setCenter(data.getCoordinate());
      if (map.getZoom() < 14) {
        map.setZoom(14);
      }
    }
  }

  /**
   * Adds a listener for feature clicks.
   *
   * @param listener receives the property ID when a feature is clicked
   */
  public void addFeatureClickListener(Consumer<String> listener) {
    featureClickListeners.add(listener);
  }

  /**
   * Adds a listener for map background clicks.
   *
   * @param listener called when the map background is clicked
   */
  public void addMapClickListener(Runnable listener) {
    mapClickListeners.add(listener);
  }

  /**
   * Gets the currently selected property ID.
   *
   * @return the selected property ID, or null if none
   */
  public String getSelectedPropertyId() {
    return selectedPropertyId;
  }

  /**
   * Sets the visibility of markers based on a set of visible property IDs.
   *
   * <p>Markers for properties not in the visibleIds set will be hidden, while markers for
   * properties in the set will be shown.
   *
   * @param visibleIds the set of property IDs that should be visible
   */
  public void setMarkerVisibility(Set<String> visibleIds) {
    for (var entry : propertyData.entrySet()) {
      String propertyId = entry.getKey();
      PropertyMarkerData data = entry.getValue();
      boolean shouldBeVisible = visibleIds.contains(propertyId);

      if (shouldBeVisible && !data.isVisible()) {
        boolean isSelected = propertyId.equals(selectedPropertyId);
        Style markerStyle = createMarkerStyle(data.getPropertyType(), isSelected);
        MarkerFeature marker =
            MarkerFeature.create().coordinate(data.getCoordinate()).style(markerStyle).build();
        map.addMarker(marker);
        data.setActiveMarker(marker);
      } else if (!shouldBeVisible && data.isVisible()) {
        map.removeFeature(data.getActiveMarker());
        data.setActiveMarker(null);
      }
    }
  }

  private static class PropertyMarkerData {
    private final Coordinate coordinate;
    private final String propertyType;
    private MarkerFeature activeMarker;

    PropertyMarkerData(Coordinate coordinate, String propertyType) {
      this.coordinate = coordinate;
      this.propertyType = propertyType;
    }

    Coordinate getCoordinate() {
      return coordinate;
    }

    String getPropertyType() {
      return propertyType;
    }

    MarkerFeature getActiveMarker() {
      return activeMarker;
    }

    void setActiveMarker(MarkerFeature marker) {
      this.activeMarker = marker;
    }

    boolean isVisible() {
      return activeMarker != null;
    }
  }
}
