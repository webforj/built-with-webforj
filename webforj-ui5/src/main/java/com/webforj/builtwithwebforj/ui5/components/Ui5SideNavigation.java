package com.webforj.builtwithwebforj.ui5.components;

import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.Element;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.EventName;
import com.webforj.component.element.annotation.EventOptions;
import com.webforj.component.element.annotation.EventOptions.EventData;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.component.event.ComponentEvent;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasSize;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasVisibility;
import com.webforj.dispatcher.EventListener;
import com.webforj.dispatcher.ListenerRegistration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The {@code ui5-side-navigation} element with its {@code ui5-side-navigation-item} children.
 *
 * <p>
 * Each item has a key. The navigation reports the key of the item the user picked, and
 * {@link #select(String)} highlights the item with a given key. The items show icons, so the entry
 * is a file in {@code src/main/frontend} that loads the icon collection before the navigation.
 * </p>
 */
@BundlePackage(value = Ui5.FIORI_PACKAGE, version = Ui5.VERSION)
@BundlePackage(value = Ui5.ICONS_PACKAGE, version = Ui5.VERSION)
@BundleEntry("ui5/side-navigation.js")
@NodeName("ui5-side-navigation")
public class Ui5SideNavigation extends ElementComposite
    implements HasVisibility<Ui5SideNavigation>, HasSize<Ui5SideNavigation>,
    HasClassName<Ui5SideNavigation>, HasStyle<Ui5SideNavigation> {
  private static final String KEY = "data-key";

  private final PropertyDescriptor<Boolean> collapsedProp =
      PropertyDescriptor.property("collapsed", false);
  private final Map<String, Element> items = new LinkedHashMap<>();
  private String selectedKey = "";

  /**
   * Adds an item.
   *
   * @param key identifies the item
   * @param text the text of the item
   * @param icon the name of an icon of the SAP icon collection
   * @return this navigation
   */
  public Ui5SideNavigation addItem(String key, String text, String icon) {
    Element item = new Element("ui5-side-navigation-item");
    item.setAttribute(KEY, key);
    item.setProperty("text", text);
    item.setProperty("icon", icon);
    items.put(key, item);
    getElement().add(item);
    return this;
  }

  /**
   * Highlights the item with the given key and no other.
   *
   * @param key the key of the item
   * @return this navigation
   */
  public Ui5SideNavigation select(String key) {
    selectedKey = key;
    items.forEach((itemKey, item) -> item.setProperty("selected", itemKey.equals(key)));
    return this;
  }

  /**
   * Gets the key of the highlighted item.
   *
   * @return the key
   */
  public String getSelectedKey() {
    return selectedKey;
  }

  /**
   * Shrinks the navigation to its icons, or shows the texts again.
   *
   * @param collapsed true to show only the icons
   * @return this navigation
   */
  public Ui5SideNavigation setCollapsed(boolean collapsed) {
    set(collapsedProp, collapsed);
    return this;
  }

  /**
   * Checks whether the navigation shows only its icons.
   *
   * @return true when collapsed
   */
  public boolean isCollapsed() {
    return get(collapsedProp);
  }

  /**
   * Adds a listener for the item the user picks.
   *
   * @param listener the listener
   * @return the registration
   */
  public ListenerRegistration<SelectEvent> onSelect(EventListener<SelectEvent> listener) {
    return addEventListener(SelectEvent.class, listener);
  }

  /**
   * Fired when the user clicks an item. The click is used and not the selection change of UI5,
   * because a click on the item that is already highlighted has to navigate too. The browser
   * sends only the key of that item along.
   */
  @EventName("click")
  @EventOptions(filter = "event.target.hasAttribute('" + KEY + "')",
      data = @EventData(key = "key", exp = "event.target.getAttribute('" + KEY + "')"))
  public static final class SelectEvent extends ComponentEvent<Ui5SideNavigation> {

    /**
     * Creates the event.
     *
     * @param source the navigation
     * @param data the event data
     */
    public SelectEvent(Ui5SideNavigation source, Map<String, Object> data) {
      super(source, data);
    }

    /**
     * Gets the key of the picked item.
     *
     * @return the key
     */
    public String getKey() {
      return String.valueOf(getData().get("key"));
    }
  }
}
