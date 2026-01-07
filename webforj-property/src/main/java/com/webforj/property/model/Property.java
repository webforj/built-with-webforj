package com.webforj.property.model;

/**
 * Property data model for real estate listings.
 *
 * <p>Immutable record containing all property information including location, pricing, features,
 * and listing status.
 *
 * @param id unique property identifier
 * @param type property type
 * @param status listing status
 * @param price listing price in dollars
 * @param street street address
 * @param city city name
 * @param state state abbreviation
 * @param zip ZIP code
 * @param longitude geographic longitude coordinate
 * @param latitude geographic latitude coordinate
 * @param beds number of bedrooms
 * @param baths number of bathrooms
 * @param sqft square footage
 * @param lotSize lot size in square feet
 * @param yearBuilt year the property was built
 * @param description property description text
 * @param imageUrl URL to property image
 */
public record Property(
    String id,
    PropertyType type,
    PropertyStatus status,
    double price,
    String street,
    String city,
    String state,
    String zip,
    double longitude,
    double latitude,
    Integer beds,
    Integer baths,
    int sqft,
    Integer lotSize,
    int yearBuilt,
    String description,
    String imageUrl) {

  /**
   * Gets the formatted price string.
   *
   * @return price formatted as $xxx,xxx
   */
  public String getFormattedPrice() {
    return String.format("$%,.0f", price);
  }

  /**
   * Gets the price per square foot.
   *
   * @return price per sqft
   */
  public double getPricePerSqft() {
    return price / sqft;
  }

  /**
   * Gets the formatted price per square foot.
   *
   * @return formatted price per sqft
   */
  public String getFormattedPricePerSqft() {
    return String.format("$%,.0f/sqft", getPricePerSqft());
  }
}
