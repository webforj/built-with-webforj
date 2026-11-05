package com.webforj.builtwithwebforj.tablesearch.search;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductCatalog {
  private final List<Product> products = List.of(
      new Product("ThinkBook 14", "Laptops", new BigDecimal("899"), 12),
      new Product("ZenBook Pro", "Laptops", new BigDecimal("1499"), 3),
      new Product("Aspire Go", "Laptops", new BigDecimal("549"), 0),
      new Product("MacBook Air", "Laptops", new BigDecimal("1199"), 7),
      new Product("Galaxy Tab S9", "Tablets", new BigDecimal("799"), 5),
      new Product("iPad Mini", "Tablets", new BigDecimal("499"), 0),
      new Product("MX Keys", "Keyboards", new BigDecimal("109"), 40),
      new Product("K380 Compact", "Keyboards", new BigDecimal("39"), 0),
      new Product("Keychron K8", "Keyboards", new BigDecimal("89"), 15),
      new Product("UltraSharp 27", "Monitors", new BigDecimal("429"), 9),
      new Product("Odyssey G5", "Monitors", new BigDecimal("299"), 0),
      new Product("MX Master 3S", "Mice", new BigDecimal("99"), 25),
      new Product("Pebble Mouse", "Mice", new BigDecimal("19"), 60));

  public List<Product> all() {
    return products;
  }

  public List<String> categories() {
    return products.stream().map(Product::category).distinct().toList();
  }
}
