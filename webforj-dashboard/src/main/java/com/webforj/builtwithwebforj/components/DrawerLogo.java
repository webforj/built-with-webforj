package com.webforj.builtwithwebforj.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Span;
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

    // Create a sleek modern design
    Div logoContainer = new Div();
    logoContainer.addClassName("drawer-logo__container");

    // Create animated gradient orb with crypto logos
    Div gradientOrb = new Div();
    gradientOrb.addClassName("drawer-logo__orb");

    // Array of crypto logo file names
    String[] cryptoLogos = {
        "ethereum-eth-logo.svg",
        "cardano-ada-logo.svg",
        "near-protocol-near-logo.svg",
        "tron-trx-logo.svg",
        "vechain-vet-logo.svg",
        "xrp-xrp-logo.svg"
    };

    // Create div elements with CSS background images
    for (int i = 0; i < cryptoLogos.length; i++) {
      Div cryptoIcon = new Div();
      cryptoIcon.addClassName("drawer-logo__crypto-icon", "drawer-logo__crypto-icon--" + i, "crypto-logo-" + i);
      gradientOrb.add(cryptoIcon);
    }

    // Add brand text
    H2 brandName = new H2("CryptoTracker");
    brandName.addClassName("drawer-logo__brand");

    // Add tagline
    Span tagline = new Span("Real-time insights");
    tagline.addClassName("drawer-logo__tagline");

    logoContainer.add(gradientOrb);
    self.add(logoContainer, brandName, tagline);
  }
}