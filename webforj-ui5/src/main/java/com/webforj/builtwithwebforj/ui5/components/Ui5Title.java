package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasText;
import com.webforj.concern.HasVisibility;

/**
 * The {@code ui5-title} element, the heading of a page.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Title.js")
@NodeName("ui5-title")
public class Ui5Title extends ElementComposite implements HasText<Ui5Title>,
    HasVisibility<Ui5Title>, HasClassName<Ui5Title>, HasStyle<Ui5Title> {

  /**
   * Creates a second level heading.
   *
   * @param text the heading text
   */
  public Ui5Title(String text) {
    setText(text);
    getElement().setProperty("level", "H2");
    getElement().setProperty("size", "H2");
  }
}
