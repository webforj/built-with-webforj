package com.webforjgeolocation.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;

public class FleuronDivider extends Composite<Div> {

  public static final String FLEURON = "❧";
  public static final String SPARKLE = "✦";

  private final Div self = getBoundComponent();
  private final Span glyph = new Span();

  public FleuronDivider() {
    this(FLEURON);
  }

  public FleuronDivider(String glyphChar) {
    self.addClassName("divider");
    glyph.addClassName("fleuron");
    glyph.setText(glyphChar);
    self.add(glyph);
  }

  public FleuronDivider setGlyph(String glyphChar) {
    glyph.setText(glyphChar);
    return this;
  }
}
