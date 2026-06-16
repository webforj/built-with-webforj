package com.webforjgeolocation.geo;

public enum Unit {
  KM("km", "kilometres", 1.0),
  MI("mi", "miles", 0.621371),
  NM("nm", "nautical", 0.539957);

  private final String label;
  private final String longLabel;
  private final double factor;

  Unit(String label, String longLabel, double factor) {
    this.label = label;
    this.longLabel = longLabel;
    this.factor = factor;
  }

  public String label() {
    return label;
  }

  public String longLabel() {
    return longLabel;
  }

  public double factor() {
    return factor;
  }
}
