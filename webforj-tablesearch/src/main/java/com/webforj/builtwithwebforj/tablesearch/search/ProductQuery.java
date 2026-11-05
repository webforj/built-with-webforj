package com.webforj.builtwithwebforj.tablesearch.search;

import java.math.BigDecimal;

public record ProductQuery(
    String category,
    String nameContains,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    Boolean inStock) {

  /**
   * Treats blank text from the model the same as a field it left out.
   */
  public ProductQuery {
    category = blankToNull(category);
    nameContains = blankToNull(nameContains);
  }

  private static String blankToNull(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }
}
