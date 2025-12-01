package com.webforj.builtwithwebforj.ghostai.component;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.concern.HasStyle;
import com.webforj.concern.HasVisibility;

public class ChatWelcomeMessage extends Composite<FlexLayout>
    implements HasVisibility<ChatWelcomeMessage>, HasStyle<ChatWelcomeMessage> {
  private final FlexLayout self = getBoundComponent();

  public ChatWelcomeMessage() {
    Icon icon = TablerIcon.create("ghost", TablerIcon.Variate.FILLED);
    icon.setStyle("color", "var(--dwc-color-primary)").setStyle("fontSize", "72px");

    Div iconWrapper = new Div();
    iconWrapper.add(icon);
    iconWrapper.setStyle("animation", "float 3s ease-in-out infinite");

    H2 title = new H2("ghost:ai");
    title.setStyle("margin", "0")
        .setStyle("fontWeight", "600")
        .setStyle("fontFamily", "monospace")
        .setStyle("fontSize", "24px");

    Paragraph p = new Paragraph("I know things. Spooky things. Ask me about webforJ and watch me haunt your codebase.");
    p.setStyle("margin", "0")
        .setStyle("maxWidth", "400px");

    self.setDirection(FlexDirection.COLUMN)
        .setAlignment(FlexAlignment.CENTER)
        .setJustifyContent(FlexJustifyContent.CENTER)
        .setSpacing("var(--dwc-space-m)")
        .setStyle("textAlign", "center")
        .add(iconWrapper, title, p);
  }
}
