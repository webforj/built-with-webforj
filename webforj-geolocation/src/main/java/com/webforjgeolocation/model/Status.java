package com.webforjgeolocation.model;

public enum Status {
  STANDING("Standing"),
  RUINS("Ruins"),
  DESTROYED("Destroyed");

  private final String label;

  Status(String label) {
    this.label = label;
  }

  public String label() {
    return label;
  }

  public String cssClass() {
    return name().toLowerCase();
  }
}
