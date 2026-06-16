package com.webforjgeolocation.components;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;

public class FramedPanel extends Composite<Div> {

  private static final String CORNER_GLYPH = "★";

  private final Div self = getBoundComponent();

  public FramedPanel() {
    self.addClassName("framed");
    self.add(
        corner("corner-tl"),
        corner("corner-tr"),
        corner("corner-bl"),
        corner("corner-br"));
  }

  private static Span corner(String position) {
    Span s = new Span();
    s.addClassName("corner");
    s.addClassName(position);
    s.setText(CORNER_GLYPH);
    return s;
  }

  public FramedPanel addContent(Component... components) {
    self.add(components);
    return this;
  }
}
