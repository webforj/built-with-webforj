package com.webforj.builtwithwebforj.ui5.customers;

/**
 * Where a customer stands with the company.
 */
public enum Status {
  LEAD("Lead"), ACTIVE("Active"), PAUSED("Paused");

  private final String label;

  Status(String label) {
    this.label = label;
  }

  /**
   * Gets the text shown to the user.
   *
   * @return the label
   */
  public String getLabel() {
    return label;
  }
}
