package com.webforj.builtwithwebforj.tablesearch.search;

import java.math.BigDecimal;

public record Product(String name, String category, BigDecimal price, int stock) {
}
