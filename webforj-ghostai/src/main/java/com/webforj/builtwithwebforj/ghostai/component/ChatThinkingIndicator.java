package com.webforj.builtwithwebforj.ghostai.component;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Span;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.component.icons.DwcIcon;
import com.webforj.component.icons.Icon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class ChatThinkingIndicator extends Composite<FlexLayout>
    implements HasStyle<ChatThinkingIndicator>, HasClassName<ChatThinkingIndicator> {

  public ChatThinkingIndicator() {
    Icon spinner = DwcIcon.ANIMATED_SPINNER.create();
    spinner.setStyle("fontSize", "20px");

    getBoundComponent()
        .setAlignment(FlexAlignment.CENTER)
        .setSpacing("var(--dwc-space-s)")
        .setStyle("padding", "var(--dwc-space-m)")
        .add(spinner, new Span("Thinking..."));
  }
}
