package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.component.element.ElementCompositeContainer;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasEnablement;
import com.webforj.concern.HasLabel;
import com.webforj.concern.HasRequired;
import com.webforj.concern.HasSize;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasValue;
import com.webforj.concern.HasVisibility;
import com.webforj.data.event.ValueChangeEvent;
import com.webforj.dispatcher.EventListener;
import com.webforj.dispatcher.ListenerRegistration;

/**
 * Base class of the UI5 form controls.
 *
 * <p>
 * It implements {@link HasValue}, the contract webforJ data binding works with, so a UI5 control
 * binds to a bean property the same way a built-in webforJ field does. A subclass maps its value to
 * the element in {@link #getValue()} and {@link #setValue(Object)}, and calls
 * {@code watchValue} with the browser event that reports a change.
 * </p>
 *
 * @param <T> the component type
 * @param <V> the value type
 */
public abstract class Ui5Field<T extends Ui5Field<T, V>, V> extends ElementCompositeContainer
    implements HasValue<T, V>, HasLabel<T>, HasRequired<T>, HasEnablement<T>, HasVisibility<T>,
    HasSize<T>, HasClassName<T>, HasStyle<T> {

  private final PropertyDescriptor<String> accessibleNameProp =
      PropertyDescriptor.property("accessibleName", "");
  private final PropertyDescriptor<Boolean> requiredProp =
      PropertyDescriptor.property("required", false);

  /**
   * Creates the field.
   *
   * @param label the label
   */
  protected Ui5Field(String label) {
    setLabel(label);
  }

  /**
   * Follows the value the user enters. When the browser fires the event, webforJ stores the
   * property the element reports, and then the value change listeners are told.
   *
   * @param property the element property that holds the value
   * @param event the name of the browser event that reports a new value
   */
  protected void watchValue(PropertyDescriptor<?> property, String event) {
    synchronize(property, event);
    getElement().addEventListener(event,
        e -> getEventDispatcher().dispatchEvent(new ValueChangeEvent<>(this, getValue())));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ListenerRegistration<ValueChangeEvent<V>> addValueChangeListener(
      EventListener<ValueChangeEvent<V>> listener) {
    return getEventDispatcher().addListener(ValueChangeEvent.class, listener);
  }

  /**
   * Sets the label. UI5 controls show no label themselves, so it becomes the accessible name. A
   * view shows it with a {@link Ui5Label} next to the control.
   *
   * @param label the label
   * @return this field
   */
  @Override
  public T setLabel(String label) {
    set(accessibleNameProp, label);
    return self();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLabel() {
    return get(accessibleNameProp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T setRequired(boolean required) {
    set(requiredProp, required);
    return self();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRequired() {
    return get(requiredProp);
  }

  /**
   * Gets this field as its own type, for the fluent setters.
   *
   * @return this field
   */
  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }
}
