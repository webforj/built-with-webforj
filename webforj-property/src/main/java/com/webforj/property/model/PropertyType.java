package com.webforj.property.model;

/**
 * Property type enumeration for real estate listings.
 *
 * <p>Each type has an associated display name and marker icon identifier.
 */
public enum PropertyType {

  /** Single-family house */
  HOUSE("House", "house"),

  /** Apartment unit */
  APARTMENT("Apartment", "apartment"),

  /** Condominium */
  CONDO("Condo", "condo"),

  /** Commercial property */
  COMMERCIAL("Commercial", "commercial");

  private final String displayName;
  private final String iconId;

  PropertyType(String displayName, String iconId) {
    this.displayName = displayName;
    this.iconId = iconId;
  }

  /**
   * Gets the human-readable display name.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Gets the icon identifier for marker styling.
   *
   * @return the icon ID (e.g., "house" for marker-house.svg)
   */
  public String getIconId() {
    return iconId;
  }
}
