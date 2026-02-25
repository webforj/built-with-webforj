package com.webforj.builtwithwebforj.ghostai.component;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasVisibility;

public class ChatHeader extends Composite<Div>
    implements HasVisibility<ChatHeader>, HasStyle<ChatHeader>, HasClassName<ChatHeader> {
  private final Div self = getBoundComponent();

  public ChatHeader() {
    Icon icon = TablerIcon.create("ghost", TablerIcon.Variate.FILLED);
    icon.setStyle("color", "var(--dwc-color-primary)")
        .setStyle("fontSize", "24px");

    H1 title = new H1("ghost:ai");
    title.setStyle("margin", "0")
        .setStyle("fontWeight", "600")
        .setStyle("fontFamily", "monospace")
        .setStyle("fontSize", "18px");

    FlexLayout layout = FlexLayout.create(icon, title)
        .horizontal()
        .align().center()
        .build();
    layout.setSpacing("var(--dwc-space-s)");

    self.setStyle("padding", "var(--dwc-space-m) var(--dwc-space-xl)")
        .setStyle("borderBottom", "1px solid var(--dwc-color-default)")
        .add(layout);

    setVisible(false);
  }
}
