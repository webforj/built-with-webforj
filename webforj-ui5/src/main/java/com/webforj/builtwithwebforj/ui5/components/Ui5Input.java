package com.webforj.builtwithwebforj.ui5.components;

import com.google.gson.annotations.SerializedName;
import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.concern.HasPlaceholder;

/**
 * The {@code ui5-input} element. Its value follows every keystroke.
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Input.js")
@NodeName("ui5-input")
public class Ui5Input extends Ui5ValidatedField<Ui5Input, String>
    implements HasPlaceholder<Ui5Input> {

  /** What the user types. Each constant is sent to the browser as the name UI5 expects. */
  public enum Type {
    @SerializedName("Text")
    TEXT,

    @SerializedName("Email")
    EMAIL,

    @SerializedName("Search")
    SEARCH
  }

  private final PropertyDescriptor<String> valueProp = PropertyDescriptor.property("value", "");
  private final PropertyDescriptor<Type> typeProp =
      PropertyDescriptor.property("type", Type.TEXT);
  private final PropertyDescriptor<String> placeholderProp =
      PropertyDescriptor.property("placeholder", "");
  private final PropertyDescriptor<Boolean> showClearIconProp =
      PropertyDescriptor.property("showClearIcon", false);

  /**
   * Creates an input.
   *
   * @param label the label
   */
  public Ui5Input(String label) {
    super(label);
    watchValue(valueProp, "input");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue() {
    return get(valueProp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Ui5Input setValue(String value) {
    set(valueProp, value == null ? "" : value);
    return this;
  }

  /**
   * Sets what the user types.
   *
   * @param type the type
   * @return this input
   */
  public Ui5Input setType(Type type) {
    set(typeProp, type);
    return this;
  }

  /**
   * Sets the hint shown while the input is empty.
   *
   * @param placeholder the placeholder
   * @return this input
   */
  @Override
  public Ui5Input setPlaceholder(String placeholder) {
    set(placeholderProp, placeholder);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlaceholder() {
    return get(placeholderProp);
  }

  /**
   * Shows an icon that empties the input.
   *
   * @param showClearIcon true to show the icon
   * @return this input
   */
  public Ui5Input setShowClearIcon(boolean showClearIcon) {
    set(showClearIconProp, showClearIcon);
    return this;
  }
}
