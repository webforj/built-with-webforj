package org.example.components.news;

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

  // Stock images for crypto/finance news
  private static final String[] STOCK_IMAGES = {
      "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=400&h=200&fit=crop",
      "https://images.unsplash.com/photo-1639762681485-074b7f938ba0?w=400&h=200&fit=crop",
      "https://images.unsplash.com/photo-1640340434855-6084b1f4901c?w=400&h=200&fit=crop",
      "https://images.unsplash.com/photo-1640826843968-011c0d0c4fbb?w=400&h=200&fit=crop",
      "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=200&fit=crop"
  };

  public NewsCard(String title, String description, String source, String timeAgo, String url) {
    self.addClassName("news-card")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-m)");
    // Add image (random stock image)
    Div imageDiv = new Div();
    imageDiv.addClassName("news-image");
    String imageUrl = STOCK_IMAGES[(int) (Math.random() * STOCK_IMAGES.length)];
    imageDiv.setStyle("background-image", "url('" + imageUrl + "')");

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