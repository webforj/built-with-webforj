package com.webforjgeolocation.model;

public enum Era {
  ANCIENT("Ancient", "var(--era-ancient)"),
  MEDIEVAL("Medieval", "var(--era-medieval)"),
  MODERN("Modern", "var(--era-modern)");

  private final String label;
  private final String accentVar;

  Era(String label, String accentVar) {
    this.label = label;
    this.accentVar = accentVar;
  }

  public String label() {
    return label;
  }

  public String accentVar() {
    return accentVar;
  }

  public String cssClass() {
    return name().toLowerCase();
  }
}
