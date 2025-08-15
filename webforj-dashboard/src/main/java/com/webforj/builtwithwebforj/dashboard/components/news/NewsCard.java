package com.webforj.builtwithwebforj.dashboard.components.news;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class NewsCard extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public NewsCard(String title, String description, String source, String timeAgo, String url, String imageUrl) {
    self.addClassName("news-card")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-m)");
    // Add image
    Div imageDiv = new Div();
    imageDiv.addClassName("news-image");
    if (imageUrl != null && !imageUrl.isEmpty()) {
      imageDiv.setStyle("background-image", "url('" + imageUrl + "')");
    }

    // Content container
    FlexLayout contentDiv = new FlexLayout();
    contentDiv.addClassName("news-content")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-s)");

    // Title
    H3 titleElement = new H3(title);

    // Description
    Paragraph descElement = new Paragraph(description);

    // Meta information container
    FlexLayout metaDiv = new FlexLayout();
    metaDiv.addClassName("news-meta")
        .setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER);

    Span sourceSpan = new Span(source);
    sourceSpan.addClassName("news-source");

    Span timeSpan = new Span(timeAgo);
    timeSpan.addClassName("news-time");

    metaDiv.add(sourceSpan, timeSpan);

    contentDiv.add(titleElement, descElement, metaDiv);

    self.add(imageDiv, contentDiv);
  }
}