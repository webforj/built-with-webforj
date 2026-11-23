package com.webforj.builtwithwebforj.ui5.components;

/**
 * The UI5 Web Components npm packages every component in this package is written against.
 *
 * <p>
 * A component names the package it needs with {@code @BundlePackage} and the module that registers
 * its custom element with {@code @BundleEntry}. The webforJ bundler installs the packages at build
 * time, and the browser loads a module the first time its component is created.
 * </p>
 */
final class Ui5 {
  /** The core components, such as the button and the input. */
  static final String PACKAGE = "@ui5/webcomponents";

  /** The Fiori components, such as the shell bar and the side navigation. */
  static final String FIORI_PACKAGE = "@ui5/webcomponents-fiori";

  /** The SAP icon collection. */
  static final String ICONS_PACKAGE = "@ui5/webcomponents-icons";

  /** One version range for all three packages, because UI5 releases them together. */
  static final String VERSION = "^2.28.1";

  private Ui5() {}
}
