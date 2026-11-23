package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.Component;
import com.webforj.component.element.ElementCompositeContainer;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.component.element.annotation.Synchronize;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasVisibility;

/**
 * The {@code ui5-dialog} element. Its content goes in with {@code add} and its buttons with
 * {@link #addToFooter(Component...)}.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Dialog.js")
@NodeName("ui5-dialog")
public class Ui5Dialog extends ElementCompositeContainer
    implements HasVisibility<Ui5Dialog>, HasClassName<Ui5Dialog>, HasStyle<Ui5Dialog> {
  private final PropertyDescriptor<String> headerTextProp =
      PropertyDescriptor.property("headerText", "");

  // The user can also close the dialog with the Escape key, and the close event reports that.
  @Synchronize("close")
  private final PropertyDescriptor<Boolean> openProp = PropertyDescriptor.property("open", false);

  /**
   * Creates a closed dialog.
   *
   * @param headerText the title of the dialog
   */
  public Ui5Dialog(String headerText) {
    set(headerTextProp, headerText);
  }

  /**
   * Adds components to the footer, where UI5 places the buttons of a dialog.
   *
   * @param components the components
   * @return this dialog
   */
  public Ui5Dialog addToFooter(Component... components) {
    getElement().add("footer", components);
    return this;
  }

  /**
   * Opens the dialog.
   *
   * @return this dialog
   */
  public Ui5Dialog open() {
    set(openProp, true);
    return this;
  }

  /**
   * Closes the dialog.
   *
   * @return this dialog
   */
  public Ui5Dialog close() {
    set(openProp, false);
    return this;
  }

  /**
   * Checks whether the dialog is open.
   *
   * @return true when the dialog is open
   */
  public boolean isOpened() {
    return get(openProp);
  }
}
