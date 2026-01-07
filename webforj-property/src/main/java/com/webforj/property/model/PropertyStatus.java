package com.webforj.property.model;

/** Property listing status enumeration. */
public enum PropertyStatus {

  /** Property is actively for sale */
  FOR_SALE("For Sale"),

  /** Sale is pending/under contract */
  PENDING("Pending"),

  /** Property has been sold */
  SOLD("Sold");

  private final String displayName;

  PropertyStatus(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets the human-readable display name.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }
}
