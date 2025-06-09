package org.example.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexDirection;
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
    // Simple card for backward compatibility
    self.setDirection(FlexDirection.COLUMN);
    self.addClassName("news-card");
    self.setStyle("background", "var(--dwc-surface-1)")
        .setStyle("border", "1px solid var(--dwc-color-gray-95)")
        .setStyle("border-radius", "var(--dwc-border-radius-m)")
        .setStyle("padding", "var(--dwc-space-m)")
        .setStyle("cursor", "pointer")
        .setStyle("transition", "all var(--dwc-transition-medium) ease");
    
    // Add image (random stock image)
    Div imageDiv = new Div();
    imageDiv.addClassName("news-image");
    String imageUrl = STOCK_IMAGES[(int) (Math.random() * STOCK_IMAGES.length)];
    imageDiv.setStyle("background-image", "url('" + imageUrl + "')");
    
    // Content container
    Div contentDiv = new Div();
    contentDiv.addClassName("news-content");
    
    // Title
    H3 titleElement = new H3(title);

    // Description
    Paragraph descElement = new Paragraph(description);

    // Meta information container
    Div metaDiv = new Div();
    metaDiv.addClassName("news-meta");
    
    Span sourceSpan = new Span(source);
    sourceSpan.addClassName("news-source");
    
    Span timeSpan = new Span(timeAgo);
    timeSpan.addClassName("news-time");
    
    metaDiv.add(sourceSpan, timeSpan);
    
    // Add content to content container
    contentDiv.add(titleElement, descElement, metaDiv);

    // Add all elements to card
    self.add(imageDiv, contentDiv);
  }
}