package org.example.components.analytics;

import com.webforj.component.Composite;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;

public class ChartCard extends Composite<Div> {
  private final Div self = getBoundComponent();
  private GoogleChart chart;

  public ChartCard(String title, GoogleChart chart) {
    this(title, chart, false);
  }

  public ChartCard(String title, GoogleChart chart, boolean fullWidth) {
    this.chart = chart;
    self.addClassName("analytics-view__chart-card");
    if (fullWidth) {
      self.addClassName("analytics-view__chart-card--full-width");
    }
    
    H3 cardTitle = new H3(title);
    cardTitle.addClassName("analytics-view__chart-title");
    
    chart.addClassName("analytics-view__chart");
    
    self.add(cardTitle, chart);
  }
  
  /**
   * Gets the GoogleChart instance for this card
   * @return the chart component
   */
  public GoogleChart getChart() {
    return chart;
  }
}