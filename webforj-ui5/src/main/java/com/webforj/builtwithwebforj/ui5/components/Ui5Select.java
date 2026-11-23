package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.Element;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.NodeName;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The {@code ui5-select} element with its {@code ui5-option} children.
 *
 * <p>
 * The select holds Java objects, for example the constants of an enum. The browser only knows the
 * position of each option, and the select turns the chosen position back into the object.
 * </p>
 *
 * @param <V> the type of the items to choose from
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundleEntry("@ui5/webcomponents/dist/Select.js")
@BundleEntry("@ui5/webcomponents/dist/Option.js")
@NodeName("ui5-select")
public class Ui5Select<V> extends Ui5ValidatedField<Ui5Select<V>, V> {
  private final PropertyDescriptor<String> valueProp = PropertyDescriptor.property("value", "0");
  private final List<Element> options = new ArrayList<>();
  private List<V> items = List.of();

  /**
   * Creates a select.
   *
   * @param label the label
   */
  public Ui5Select(String label) {
    super(label);
    watchValue(valueProp, "change");
  }

  /**
   * Sets the items to choose from. The first one is selected.
   *
   * @param items the items
   * @param text gives the text shown for an item
   * @return this select
   */
  public Ui5Select<V> setItems(List<V> items, Function<V, String> text) {
    this.items = List.copyOf(items);
    getElement().remove(options.toArray(Element[]::new));
    options.clear();
    for (int i = 0; i < this.items.size(); i++) {
      Element option = new Element("ui5-option");
      option.setProperty("value", String.valueOf(i));
      option.setText(text.apply(this.items.get(i)));
      options.add(option);
    }
    getElement().add(options.toArray(Element[]::new));
    set(valueProp, "0");

    return this;
  }

  /**
   * Gets the items to choose from.
   *
   * @return the items
   */
  public List<V> getItems() {
    return items;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public V getValue() {
    int index = Integer.parseInt(get(valueProp));
    return index < items.size() ? items.get(index) : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Ui5Select<V> setValue(V value) {
    set(valueProp, String.valueOf(Math.max(0, items.indexOf(value))));
    return this;
  }
}
