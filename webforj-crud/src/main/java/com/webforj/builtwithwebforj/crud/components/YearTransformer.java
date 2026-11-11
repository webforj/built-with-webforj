package com.webforj.builtwithwebforj.crud.components;

import com.webforj.data.transformation.TransformationException;
import com.webforj.data.transformation.transformer.Transformer;

/**
 * Converts between the {@code Double} a NumberField holds and the whole year the artist stores.
 */
public class YearTransformer implements Transformer<Double, Integer> {

  @Override
  public Integer transformToModel(Double componentValue) {
    if (componentValue == null) {
      return null;
    }
    if (componentValue % 1 != 0) {
      throw new TransformationException("Year must be a whole number");
    }
    return componentValue.intValue();
  }

  @Override
  public Double transformToComponent(Integer modelValue) {
    return modelValue == null ? null : modelValue.doubleValue();
  }
}
