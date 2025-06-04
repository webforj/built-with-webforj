package org.example.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class NewsCard extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public NewsCard(String title, String description, String source, String timeAgo, String url) {
    // Title
    H3 titleElement = new H3(title);

    // Description
    Paragraph descElement = new Paragraph(description);

    // Source and time
    Div metaInfo = new Div();
    metaInfo.addClassName("news-card__meta");

    Span sourceSpan = new Span(source);

    Span separator = new Span("•");

    Span timeSpan = new Span(timeAgo);

    metaInfo.add(sourceSpan, separator, timeSpan);

    // Read more link
    Anchor readMore = new Anchor(url, "Read more →");
    readMore.setTarget("_blank");

    // Configure card layout
    self.setDirection(FlexDirection.COLUMN);
    self.addClassName("news-card");

    // Add all elements
    self.add(titleElement, descElement, metaInfo, readMore);
  }
}