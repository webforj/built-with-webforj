package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Span;
import com.webforjgeolocation.model.Era;

public class EraBadge extends Composite<Span> {

  private final Span self = getBoundComponent();
  private Era era;

  public EraBadge(Era era) {
    self.addClassName("badge");
    setEra(era);
  }

  public EraBadge setEra(Era era) {
    this.era = era;
    self.setText(era.label());
    self.setStyle("--accent", era.accentVar());
    return this;
  }

  public Era getEra() {
    return era;
  }
}
