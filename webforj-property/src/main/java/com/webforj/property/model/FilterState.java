package com.webforj.property.model;

/** Represents the current filter state for property listings. */
public class FilterState {

  private String searchText = "";
  private PropertyType propertyType = null;
  private Integer minBeds = null;
  private Integer minBaths = null;

  /**
   * Gets the search text filter.
   *
   * @return the search text
   */
  public String getSearchText() {
    return searchText;
  }

  /**
   * Sets the search text filter.
   *
   * @param searchText the search text to filter by
   */
  public void setSearchText(String searchText) {
    this.searchText = searchText != null ? searchText.trim().toLowerCase() : "";
  }

  /**
   * Gets the property type filter.
   *
   * @return the property type, or null for all types
   */
  public PropertyType getPropertyType() {
    return propertyType;
  }

  /**
   * Sets the property type filter.
   *
   * @param propertyType the property type, or null for all types
   */
  public void setPropertyType(PropertyType propertyType) {
    this.propertyType = propertyType;
  }

  /**
   * Gets the minimum beds filter.
   *
   * @return the minimum beds, or null for any
   */
  public Integer getMinBeds() {
    return minBeds;
  }

  /**
   * Sets the minimum beds filter.
   *
   * @param minBeds the minimum beds, or null for any
   */
  public void setMinBeds(Integer minBeds) {
    this.minBeds = minBeds;
  }

  /**
   * Gets the minimum baths filter.
   *
   * @return the minimum baths, or null for any
   */
  public Integer getMinBaths() {
    return minBaths;
  }

  /**
   * Sets the minimum baths filter.
   *
   * @param minBaths the minimum baths, or null for any
   */
  public void setMinBaths(Integer minBaths) {
    this.minBaths = minBaths;
  }

  /**
   * Tests if a property matches the current filter state.
   *
   * @param property the property to test
   * @return true if the property matches all active filters
   */
  public boolean matches(Property property) {
    if (!searchText.isEmpty() && !matchesSearchText(property)) {
      return false;
    }

    if (propertyType != null && property.type() != propertyType) {
      return false;
    }

    if (minBeds != null) {
      Integer propertyBeds = property.beds();
      if (propertyBeds == null || propertyBeds < minBeds) {
        return false;
      }
    }

    if (minBaths != null) {
      Integer propertyBaths = property.baths();
      if (propertyBaths == null || propertyBaths < minBaths) {
        return false;
      }
    }

    return true;
  }

  private boolean matchesSearchText(Property property) {
    return property.street().toLowerCase().contains(searchText)
        || property.city().toLowerCase().contains(searchText)
        || property.state().toLowerCase().contains(searchText)
        || property.zip().toLowerCase().contains(searchText)
        || property.description().toLowerCase().contains(searchText);
  }

  /** Resets all filters to their default state. */
  public void reset() {
    searchText = "";
    propertyType = null;
    minBeds = null;
    minBaths = null;
  }

  /**
   * Checks if any filters are currently active.
   *
   * @return true if at least one filter is set
   */
  public boolean hasActiveFilters() {
    return !searchText.isEmpty() || propertyType != null || minBeds != null || minBaths != null;
  }
}
