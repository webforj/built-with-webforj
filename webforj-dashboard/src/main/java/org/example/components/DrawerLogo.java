package org.example.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Img;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class DrawerLogo extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public DrawerLogo() {
    self.setDirection(FlexDirection.COLUMN)
        .setJustifyContent(FlexJustifyContent.CENTER)
        .setAlignment(FlexAlignment.CENTER)
        .setStyle("padding", "var(--dwc-space-l)")
        .setStyle("border-bottom", "var(--dwc-border-width) solid var(--dwc-color-gray-90)")
        .setStyle("margin-bottom", "var(--dwc-space-m)");

    // WebforJ Logo
    Img logo = new Img();
    logo.setSrc("https://documentation.webforj.com/img/webforj_icon.svg")
        .setAlt("WebforJ Logo")
        .setStyle("width", "75px")
        .setStyle("height", "75px")
        .setStyle("margin-bottom", "var(--dwc-space-s)");
    self.add(logo);
  }
}