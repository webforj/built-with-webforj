package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.concern.HasPlaceholder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The {@code ui5-date-picker} element, with a {@link LocalDate} value.
 *
 * <p>
 * The element exchanges the date in ISO format, which is what {@link LocalDate} reads and writes,
 * and shows it to the user in the format of the browser's locale. The locale data for that has to
 * be registered before the picker, so the entry is a file in {@code src/main/frontend} that loads
 * {@code Assets.js} before the picker itself.
 * </p>
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("ui5/date-picker.js")
@NodeName("ui5-date-picker")
public class Ui5DatePicker extends Ui5ValidatedField<Ui5DatePicker, LocalDate>
    implements HasPlaceholder<Ui5DatePicker> {
  private final PropertyDescriptor<String> valueProp = PropertyDescriptor.property("value", "");
  private final PropertyDescriptor<String> placeholderProp =
      PropertyDescriptor.property("placeholder", "");
  private final PropertyDescriptor<String> valueFormatProp =
      PropertyDescriptor.property("valueFormat", "");
  private final PropertyDescriptor<String> displayFormatProp =
      PropertyDescriptor.property("displayFormat", "");

  /**
   * Creates a date picker.
   *
   * @param label the label
   */
  public Ui5DatePicker(String label) {
    super(label);
    set(valueFormatProp, "yyyy-MM-dd");
    set(displayFormatProp, "medium");
    watchValue(valueProp, "change");
  }

  /**
   * Gets the date, or null when the picker is empty or the user typed something that is no date.
   */
  @Override
  public LocalDate getValue() {
    try {
      return LocalDate.parse(get(valueProp));
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  /**
   * Checks whether the picker holds text that is no date. {@link #getValue()} is null for such
   * text and for an empty picker, and this tells the two apart.
   *
   * @return true when the user typed something that is no date
   */
  public boolean hasInvalidText() {
    return !get(valueProp).isEmpty() && getValue() == null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Ui5DatePicker setValue(LocalDate value) {
    set(valueProp, value == null ? "" : value.toString());
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Ui5DatePicker setPlaceholder(String placeholder) {
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
}
