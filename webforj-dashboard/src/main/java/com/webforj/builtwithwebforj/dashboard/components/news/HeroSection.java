package com.webforj.builtwithwebforj.dashboard.components.news;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;

public class HeroSection extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  private static final String LINK_TARGET_BLANK = "_blank";
  private static final String[] HERO_IMAGES = {
      "https://images.unsplash.com/photo-1642790106117-e829e14a795f?w=1200&h=600&fit=crop",
      "https://images.unsplash.com/photo-1640340434855-6084b1f4901c?w=1200&h=600&fit=crop",
      "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=1200&h=600&fit=crop"
  };

  public HeroSection() {
    self.addClassName("news-view__hero");
    self.setDirection(FlexDirection.COLUMN);

    // Background pattern
    Div bgPattern = new Div();
    bgPattern.addClassName("news-view__hero-pattern");

    self.add(bgPattern);

    FlexLayout heroContent = new FlexLayout();
    heroContent.addClassName("news-view__hero-content")
        .setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-xl)");

    // Hero text
    FlexLayout heroText = new FlexLayout();
    heroText.addClassName("news-view__hero-text")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-l)");

    H2 heroTitle = new H2("Breaking: Bitcoin Reaches New All-Time High");
    heroTitle.addClassName("news-view__hero-title");

    Paragraph heroDesc = new Paragraph(
        "Bitcoin surged past $75,000 today as institutional adoption continues to accelerate. Market analysts predict further growth as regulatory clarity improves globally.");
    heroDesc.addClassName("news-view__hero-desc");

    Anchor readMoreBtn = new Anchor("Read Full Story →");
    readMoreBtn.addClassName("news-view__hero-button");
    readMoreBtn.setHref("https://example.com/bitcoin-surge");
    readMoreBtn.setTarget(LINK_TARGET_BLANK);

    heroText.add(heroTitle, heroDesc, readMoreBtn);

    // Hero image
    Div heroImageContainer = new Div();
    heroImageContainer.addClassName("news-view__hero-image");
    heroImageContainer.setStyle("background-image", "url('" + HERO_IMAGES[0] + "')");

    heroContent.add(heroText, heroImageContainer);
    self.add(heroContent);
  }
}
