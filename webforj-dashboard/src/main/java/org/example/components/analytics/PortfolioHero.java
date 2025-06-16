package org.example.components.analytics;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class PortfolioHero extends Composite<Div> {
  private Div self = getBoundComponent();

  public PortfolioHero() {
    initComponent();
  }

  private void initComponent() {
    self.addClassName("portfolio-hero");
    
    // Main content area with metrics
    FlexLayout heroContent = new FlexLayout();
    heroContent.addClassName("portfolio-hero__content");
    
    // Left side - Main portfolio value
    FlexLayout mainValue = new FlexLayout();
    mainValue.addClassName("portfolio-hero__main");
    mainValue.setDirection(FlexDirection.COLUMN);
    
    Paragraph welcomeText = new Paragraph("Portfolio Value");
    welcomeText.addClassName("portfolio-hero__label");
    
    H2 totalValue = new H2("$125,480.50");
    totalValue.addClassName("portfolio-hero__value");
    
    FlexLayout changeContainer = new FlexLayout();
    changeContainer.addClassName("portfolio-hero__change");
    
    Span changeValue = new Span("+ $27,280.50");
    changeValue.addClassName("portfolio-hero__change-value");
    
    Span changePercent = new Span("(+27.8%)");
    changePercent.addClassName("portfolio-hero__change-percent");
    
    changeContainer.add(changeValue, changePercent);
    
    mainValue.add(welcomeText, totalValue, changeContainer);
    
    // Right side - Quick stats grid
    FlexLayout statsGrid = new FlexLayout();
    statsGrid.addClassName("portfolio-hero__stats");
    statsGrid.setDirection(FlexDirection.COLUMN);
    
    statsGrid.add(
      createQuickStat("24h Change", "+2.8%", "trending-up"),
      createQuickStat("Total Assets", "20", "coins"),
      createQuickStat("Best Performer", "SOL +22.4%", "trophy")
    );
    
    heroContent.add(mainValue, statsGrid);
    self.add(heroContent);
  }

  private FlexLayout createQuickStat(String label, String value, String iconName) {
    FlexLayout stat = new FlexLayout();
    stat.addClassName("portfolio-hero__quick-stat");
    
    Div iconContainer = new Div();
    iconContainer.addClassName("portfolio-hero__quick-stat-icon");
    iconContainer.setHtml("<dwc-icon name=\"tabler:" + iconName + "\"></dwc-icon>");
    
    FlexLayout textContainer = new FlexLayout();
    textContainer.addClassName("portfolio-hero__quick-stat-text");
    textContainer.setDirection(FlexDirection.COLUMN);
    
    Paragraph statLabel = new Paragraph(label);
    statLabel.addClassName("portfolio-hero__quick-stat-label");
    
    Span statValue = new Span(value);
    statValue.addClassName("portfolio-hero__quick-stat-value");
    
    textContainer.add(statLabel, statValue);
    stat.add(iconContainer, textContainer);
    
    return stat;
  }
}