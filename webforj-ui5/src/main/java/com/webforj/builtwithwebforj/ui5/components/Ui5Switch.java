package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;

/**
 * The {@code ui5-switch} element, with a {@link Boolean} value.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Switch.js")
@NodeName("ui5-switch")
public class Ui5Switch extends Ui5Field<Ui5Switch, Boolean> {
  private final PropertyDescriptor<Boolean> checkedProp =
      PropertyDescriptor.property("checked", false);

  /**
   * Creates a switch.
   *
   * @param label the label
   */
  public Ui5Switch(String label) {
    super(label);
    watchValue(checkedProp, "change");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean getValue() {
    return get(checkedProp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Ui5Switch setValue(Boolean value) {
    set(checkedProp, Boolean.TRUE.equals(value));
    return this;
  }
}
