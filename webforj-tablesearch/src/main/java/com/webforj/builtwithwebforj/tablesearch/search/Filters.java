package com.webforj.builtwithwebforj.tablesearch.search;

import java.util.Locale;
import java.util.function.Predicate;

public final class Filters {
  private Filters() {
  }

  public static Predicate<Product> from(ProductQuery q) {
    Predicate<Product> filter = p -> true;
    if (q.category() != null) {
      filter = filter.and(p -> p.category().equalsIgnoreCase(q.category()));
    }
    if (q.nameContains() != null) {
      String part = q.nameContains().toLowerCase(Locale.ROOT);
      filter = filter.and(p -> p.name().toLowerCase(Locale.ROOT).contains(part));
    }
    if (q.minPrice() != null) {
      filter = filter.and(p -> p.price().compareTo(q.minPrice()) >= 0);
    }
    if (q.maxPrice() != null) {
      filter = filter.and(p -> p.price().compareTo(q.maxPrice()) <= 0);
    }
    if (q.inStock() != null) {
      filter = filter.and(p -> (p.stock() > 0) == q.inStock());
    }
    return filter;
  }
}
