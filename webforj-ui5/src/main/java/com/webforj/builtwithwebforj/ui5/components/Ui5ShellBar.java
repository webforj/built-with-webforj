package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.ElementCompositeContainer;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasVisibility;

/**
 * The {@code ui5-shellbar} element, the bar at the top of a Fiori app.
 */
@BundlePackage(value = Ui5.FIORI_PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents-fiori/dist/ShellBar.js")
@NodeName("ui5-shellbar")
public class Ui5ShellBar extends ElementCompositeContainer
    implements HasVisibility<Ui5ShellBar>, HasClassName<Ui5ShellBar>, HasStyle<Ui5ShellBar> {
  private final PropertyDescriptor<String> primaryTitleProp =
      PropertyDescriptor.property("primaryTitle", "");
  private final PropertyDescriptor<String> secondaryTitleProp =
      PropertyDescriptor.property("secondaryTitle", "");

  /**
   * Creates a shell bar.
   *
   * @param primaryTitle the name of the app
   * @param secondaryTitle the smaller text next to the name
   */
  public Ui5ShellBar(String primaryTitle, String secondaryTitle) {
    set(primaryTitleProp, primaryTitle);
    set(secondaryTitleProp, secondaryTitle);
  }

  /**
   * Places a button at the start of the bar, which is where UI5 puts the menu button.
   *
   * @param button the button
   * @return this shell bar
   */
  public Ui5ShellBar setStartButton(Ui5Button button) {
    getElement().add("startButton", button);
    return this;
  }
}
