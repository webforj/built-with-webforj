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
 * The {@code ui5-text} element, a paragraph of text.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Text.js")
@NodeName("ui5-text")
public class Ui5Text extends ElementComposite implements HasText<Ui5Text>,
    HasVisibility<Ui5Text>, HasClassName<Ui5Text>, HasStyle<Ui5Text> {

  /**
   * Creates a text.
   *
   * @param text the text
   */
  public Ui5Text(String text) {
    setText(text);
  }
}
