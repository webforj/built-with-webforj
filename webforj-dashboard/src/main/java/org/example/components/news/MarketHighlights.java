package org.example.components.news;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class MarketHighlights extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public MarketHighlights() {
    self.addClassName("news-view__section")
        .setDirection(FlexDirection.COLUMN);

    createMarketContent();
  }

  private void createMarketContent() {
    H3 marketTitle = new H3("Market Highlights");
    marketTitle.addClassName("news-view__section-title");

    FlexLayout highlights = new FlexLayout();
    highlights.addClassName("news-view__market-highlights");
    highlights.setDirection(FlexDirection.COLUMN);

    String[][] marketData = {
        { "Bitcoin", "$67,234", "+2.4%", "success" },
        { "Ethereum", "$2,891", "-0.8%", "danger" },
        { "Market Cap", "$2.1T", "+1.2%", "success" }
    };

    for (String[] data : marketData) {
      FlexLayout item = new FlexLayout();
      item.addClassName("news-view__market-item");
      item.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setAlignment(FlexAlignment.CENTER);

      FlexLayout leftSide = new FlexLayout();
      leftSide.setDirection(FlexDirection.COLUMN);

      Span name = new Span(data[0]);
      name.addClassName("news-view__market-name");

      Span price = new Span(data[1]);
      price.addClassName("news-view__market-price");

      leftSide.add(name, price);

      Span change = new Span(data[2]);
      change.addClassName("news-view__market-change");
      if (data[3].equals("success")) {
        change.addClassName("news-view__market-change--success");
      } else {
        change.addClassName("news-view__market-change--danger");
      }

      item.add(leftSide, change);
      highlights.add(item);
    }

    self.add(marketTitle, highlights);
  }

  public void updateMarketData(String[][] newData) {
    // Clear existing content and recreate with new data
    self.removeAll();
    createMarketContent();
  }
}