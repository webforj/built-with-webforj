package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.component.element.annotation.Synchronize;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasText;
import com.webforj.concern.HasVisibility;

/**
 * The {@code ui5-toast} element, a short message that goes away by itself.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Toast.js")
@NodeName("ui5-toast")
public class Ui5Toast extends ElementComposite implements HasText<Ui5Toast>,
    HasVisibility<Ui5Toast>, HasClassName<Ui5Toast>, HasStyle<Ui5Toast> {

  // The toast closes itself after a few seconds, and the close event reports that.
  @Synchronize("close")
  private final PropertyDescriptor<Boolean> openProp = PropertyDescriptor.property("open", false);

  /**
   * Shows a message.
   *
   * @param text the message
   * @return this toast
   */
  public Ui5Toast show(String text) {
    setText(text);
    set(openProp, true);
    return this;
  }

  /**
   * Checks whether the toast is showing.
   *
   * @return true while the toast is showing
   */
  public boolean isOpened() {
    return get(openProp);
  }
}
