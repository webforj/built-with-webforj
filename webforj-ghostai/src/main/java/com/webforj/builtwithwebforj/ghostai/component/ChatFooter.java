package com.webforj.builtwithwebforj.ghostai.component;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class ChatFooter extends Composite<FlexLayout> {
  private final FlexLayout self = getBoundComponent();

  public ChatFooter() {
    Anchor link = new Anchor("https://docs.webforj.com", "webforJ");
    link.setStyle("color", "var(--dwc-color-primary)")
        .setStyle("fontWeight", "600");

    Span text = new Span();
    text.add(new Span("Built with ❤️ for "), link);

    self.setJustifyContent(FlexJustifyContent.CENTER)
        .setAlignment(FlexAlignment.CENTER)
        .setStyle("padding", "var(--dwc-space-m)")
        .setStyle("fontSize", "var(--dwc-font-size-s)")
        .setStyle("color", "var(--dwc-color-default-text)")
        .setStyle("opacity", "0.7")
        .add(text);
  }
}
