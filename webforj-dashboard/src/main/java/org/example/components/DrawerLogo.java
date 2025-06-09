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
    self.addClassName("drawer-logo");
    self.setDirection(FlexDirection.COLUMN)
        .setJustifyContent(FlexJustifyContent.CENTER)
        .setAlignment(FlexAlignment.CENTER);

    // WebforJ Logo
    Img logo = new Img();
    logo.setSrc("https://documentation.webforj.com/img/webforj_icon.svg")
        .setAlt("WebforJ Logo")
        .addClassName("drawer-logo__image");
    self.add(logo);
  }
}