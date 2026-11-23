package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasRequired;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasText;
import com.webforj.concern.HasVisibility;

/**
 * The {@code ui5-label} element, the visible label of a form control.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Label.js")
@NodeName("ui5-label")
public class Ui5Label extends ElementComposite implements HasText<Ui5Label>,
    HasRequired<Ui5Label>, HasVisibility<Ui5Label>, HasClassName<Ui5Label>, HasStyle<Ui5Label> {
  private final PropertyDescriptor<Boolean> requiredProp =
      PropertyDescriptor.property("required", false);

  /**
   * Creates a label.
   *
   * @param text the label text
   */
  public Ui5Label(String text) {
    setText(text);
  }

  /**
   * Marks the label with the UI5 asterisk of a required field.
   *
   * @param required true to show the asterisk
   * @return this label
   */
  @Override
  public Ui5Label setRequired(boolean required) {
    set(requiredProp, required);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRequired() {
    return get(requiredProp);
  }
}
