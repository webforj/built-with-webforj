package com.webforj.builtwithwebforj.dashboard.components.analytics;

import com.webforj.component.Composite;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.html.elements.H3;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class ChartCard extends Composite<FlexLayout> {
  private final FlexLayout self = getBoundComponent();
  private GoogleChart chart;

  public ChartCard(String title, GoogleChart chart) {
    this(title, chart, false);
  }

  public ChartCard(String title, GoogleChart chart, boolean fullWidth) {
    this.chart = chart;
    self.addClassName("analytics-view__chart-card")
        .setDirection(FlexDirection.COLUMN);

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
   * 
   * @return the chart component
   */
  public GoogleChart getChart() {
    return chart;
  }
}