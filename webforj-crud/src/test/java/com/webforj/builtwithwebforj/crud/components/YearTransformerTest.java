package com.webforj.builtwithwebforj.crud.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.webforj.data.transformation.TransformationException;
import org.junit.jupiter.api.Test;

class YearTransformerTest {

  private final YearTransformer transformer = new YearTransformer();

  @Test
  void shouldStoreAWholeYear() {
    assertEquals(1960, transformer.transformToModel(1960d));
  }

  @Test
  void shouldShowTheStoredYearInTheField() {
    assertEquals(1960d, transformer.transformToComponent(1960));
  }

  @Test
  void shouldKeepAnEmptyValueEmpty() {
    assertNull(transformer.transformToModel(null));
    assertNull(transformer.transformToComponent(null));
  }

  @Test
  void shouldRejectAYearWithDecimals() {
    assertThrows(TransformationException.class, () -> transformer.transformToModel(1960.5));
  }
}
