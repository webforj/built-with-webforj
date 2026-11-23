package com.webforj.builtwithwebforj.ui5.components;

import com.google.gson.annotations.SerializedName;
import com.webforj.bundle.annotation.BundleEntry;
import com.webforj.bundle.annotation.BundlePackage;
import com.webforj.component.element.ElementComposite;
import com.webforj.component.element.PropertyDescriptor;
import com.webforj.component.element.annotation.EventName;
import com.webforj.component.element.annotation.NodeName;
import com.webforj.component.event.ComponentEvent;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasEnablement;
import com.webforj.concern.HasSize;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasText;
import com.webforj.concern.HasTooltip;
import com.webforj.concern.HasVisibility;
import com.webforj.dispatcher.EventListener;
import com.webforj.dispatcher.ListenerRegistration;
import java.util.Map;

/**
 * The {@code ui5-button} element.
 *
 * <p>
 * A button can show an icon, so its entry is a file in {@code src/main/frontend} that loads the
 * icon collection before the button itself.
 * </p>
 */
@BundlePackage(value = Ui5.PACKAGE, version = Ui5.VERSION)
@BundlePackage(value = Ui5.ICONS_PACKAGE, version = Ui5.VERSION)
@BundleEntry("ui5/button.js")
@NodeName("ui5-button")
public class Ui5Button extends ElementComposite
    implements HasText<Ui5Button>, HasEnablement<Ui5Button>, HasTooltip<Ui5Button>,
    HasVisibility<Ui5Button>, HasSize<Ui5Button>, HasClassName<Ui5Button>, HasStyle<Ui5Button> {

  /** How the button looks. Each constant is sent to the browser as the name UI5 expects. */
  public enum Design {
    @SerializedName("Default")
    DEFAULT,

    @SerializedName("Emphasized")
    EMPHASIZED,

    @SerializedName("Negative")
    NEGATIVE,

    @SerializedName("Transparent")
    TRANSPARENT
  }

  private final PropertyDescriptor<Design> designProp =
      PropertyDescriptor.property("design", Design.DEFAULT);
  private final PropertyDescriptor<String> iconProp = PropertyDescriptor.property("icon", "");
  private final PropertyDescriptor<String> tooltipProp =
      PropertyDescriptor.property("tooltip", "");

  /**
   * Creates a button.
   *
   * @param text the button text
   * @param design the button design
   */
  public Ui5Button(String text, Design design) {
    setText(text);
    set(designProp, design);
  }

  /**
   * Sets the icon. Any name of the SAP icon collection works.
   *
   * @param icon the icon name, for example {@code menu2}
   * @return this button
   */
  public Ui5Button setIcon(String icon) {
    set(iconProp, icon);
    return this;
  }

  /**
   * Sets the tooltip, which also names a button that shows only an icon to a screen reader.
   *
   * @param tooltipText the tooltip
   * @return this button
   */
  @Override
  public Ui5Button setTooltipText(String tooltipText) {
    set(tooltipProp, tooltipText);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTooltipText() {
    return get(tooltipProp);
  }

  /**
   * Adds a listener for the click.
   *
   * @param listener the listener
   * @return the registration
   */
  public ListenerRegistration<ClickEvent> onClick(EventListener<ClickEvent> listener) {
    return addEventListener(ClickEvent.class, listener);
  }

  /**
   * Fired when the user clicks the button.
   */
  @EventName("click")
  public static final class ClickEvent extends ComponentEvent<Ui5Button> {

    /**
     * Creates the event.
     *
     * @param source the button
     * @param data the event data
     */
    public ClickEvent(Ui5Button source, Map<String, Object> data) {
      super(source, data);
    }
  }
}
