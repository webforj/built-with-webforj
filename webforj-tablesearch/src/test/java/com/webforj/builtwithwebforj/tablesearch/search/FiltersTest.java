package com.webforj.builtwithwebforj.tablesearch.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class FiltersTest {
  private final List<Product> all = new ProductCatalog().all();

  private List<String> names(ProductQuery q) {
    return all.stream().filter(Filters.from(q)).map(Product::name).toList();
  }

  @Test
  void laptopsUnder1000InStock() {
    assertEquals(List.of("ThinkBook 14"),
        names(new ProductQuery("laptops", null, null, new BigDecimal("1000"), true)));
  }

  @Test
  void laptopsOutOfStock() {
    assertEquals(List.of("Aspire Go"),
        names(new ProductQuery("Laptops", null, null, null, false)));
  }

  @Test
  void priceRangeAndName() {
    assertEquals(List.of("MX Keys", "MX Master 3S"),
        names(new ProductQuery(null, "mx", new BigDecimal("90"), new BigDecimal("110"), null)));
  }

  @Test
  void emptyQueryMatchesEverything() {
    assertEquals(all.size(), names(new ProductQuery(null, null, null, null, null)).size());
  }

  @Test
  void unknownCategoryMatchesNothing() {
    assertEquals(List.of(), names(new ProductQuery("Notebooks", null, null, null, null)));
  }

  @Test
  void nameMatchingIgnoresTheServerLocale() {
    Locale saved = Locale.getDefault();
    Locale.setDefault(Locale.forLanguageTag("tr"));
    try {
      assertEquals(List.of("iPad Mini"),
          names(new ProductQuery(null, "IPAD", null, null, null)));
    } finally {
      Locale.setDefault(saved);
    }
  }

  @Test
  void blankTextCountsAsLeftOut() {
    ProductQuery q = new ProductQuery(" ", "", null, null, null);

    assertNull(q.category());
    assertNull(q.nameContains());
    assertEquals(all.size(), names(q).size());
  }
}
