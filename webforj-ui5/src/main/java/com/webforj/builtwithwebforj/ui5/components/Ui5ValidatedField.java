package com.webforj.builtwithwebforj.ui5.components;

import com.google.gson.annotations.SerializedName;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.html.elements.Div;
import com.webforj.concern.HasReadOnly;
import com.webforj.data.validation.InvalidAware;

/**
 * Base class of the UI5 form controls that can show an error.
 *
 * <p>
 * It implements {@link InvalidAware}, the contract webforJ data binding reports a failed validation
 * through. An invalid field gets the UI5 {@code Negative} value state and the message goes into the
 * element's {@code valueStateMessage} slot, so the error looks the way UI5 designed it.
 * </p>
 *
 * @param <T> the component type
 * @param <V> the value type
 */
public abstract class Ui5ValidatedField<T extends Ui5ValidatedField<T, V>, V>
    extends Ui5Field<T, V> implements InvalidAware<T>, HasReadOnly<T> {

  private enum ValueState {
    @SerializedName("None")
    NONE,

    @SerializedName("Negative")
    NEGATIVE
  }

  private final PropertyDescriptor<ValueState> valueStateProp =
      PropertyDescriptor.property("valueState", ValueState.NONE);
  private final PropertyDescriptor<Boolean> readonlyProp =
      PropertyDescriptor.property("readonly", false);
  private final Div message = new Div();

  /**
   * Creates the field.
   *
   * @param label the label
   */
  protected Ui5ValidatedField(String label) {
    super(label);
    getElement().add("valueStateMessage", message);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T setInvalid(boolean invalid) {
    set(valueStateProp, invalid ? ValueState.NEGATIVE : ValueState.NONE);
    return self();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInvalid() {
    return get(valueStateProp) == ValueState.NEGATIVE;
  }

  /**
   * Sets the error message. webforJ data binding joins the messages of a field with
   * {@code <br>}, so the message is HTML.
   *
   * @param message the message
   * @return this field
   */
  @Override
  public T setInvalidMessage(String message) {
    this.message.setHtml(message);
    return self();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInvalidMessage() {
    return message.getHtml();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T setReadOnly(boolean readOnly) {
    set(readonlyProp, readOnly);
    return self();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly() {
    return get(readonlyProp);
  }
}
