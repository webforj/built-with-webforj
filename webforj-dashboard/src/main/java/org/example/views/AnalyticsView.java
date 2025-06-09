package org.example.views;

import com.webforj.component.Composite;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import org.example.components.DashboardCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "analytics", outlet = MainLayout.class)
@FrameTitle("Market Analytics")
public class AnalyticsView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public AnalyticsView() {
    self.addClassName("analytics-view");
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("overflow-y", "auto");
    self.setStyle("padding", "var(--dwc-space-l)");
    
    // Create header
    createHeader();
    
    // Create metric cards
    createMetricCards();
    
    // Create charts section
    createChartsSection();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.setDirection(FlexDirection.COLUMN)
          .setStyle("margin-bottom", "var(--dwc-space-xl)");
    
    H2 title = new H2("Market Analytics");
    title.setStyle("margin", "0");
    
    Paragraph description = new Paragraph("Comprehensive market analysis and performance metrics");
    description.setStyle("color", "var(--dwc-color-default-color)");
    description.setStyle("margin", "var(--dwc-space-s) 0 0 0");
    
    header.add(title, description);
    self.add(header);
  }

  private void createMetricCards() {
    FlexLayout cardsSection = new FlexLayout();
    cardsSection.setJustifyContent(FlexJustifyContent.BETWEEN)
                .setWrap(FlexWrap.WRAP)
                .setStyle("gap", "var(--dwc-space-m)")
                .setStyle("margin-bottom", "var(--dwc-space-xl)");
    
    DashboardCard totalVolume = new DashboardCard("Total Market Volume", 152500000000.0, 12.5);
    DashboardCard avgTransaction = new DashboardCard("Avg Transaction Size", 3250.0, -2.1, GoogleChart.Type.BAR);
    DashboardCard volatilityIndex = new DashboardCard("Volatility Index", 68.5, 8.7, GoogleChart.Type.LINE);
    
    cardsSection.add(totalVolume, avgTransaction, volatilityIndex);
    self.add(cardsSection);
  }

  private void createChartsSection() {
    FlexLayout chartsContainer = new FlexLayout();
    chartsContainer.setWrap(FlexWrap.WRAP)
                   .setStyle("gap", "var(--dwc-space-l)");
    
    // Market Cap Distribution Chart
    Div marketCapChart = createChartCard("Market Cap Distribution", createPieChart());
    
    // Trading Volume History Chart
    Div volumeChart = createChartCard("24h Trading Volume", createLineChart());
    
    // Price Performance Chart
    Div performanceChart = createChartCard("Price Performance (7d)", createColumnChart());
    
    // Market Sentiment Chart
    Div sentimentChart = createChartCard("Market Sentiment", createAreaChart());
    
    chartsContainer.add(marketCapChart, volumeChart, performanceChart, sentimentChart);
    self.add(chartsContainer);
  }

  private Div createChartCard(String title, GoogleChart chart) {
    Div card = new Div();
    card.addClassName("analytics-chart-card");
    card.setStyle("flex", "1 1 400px")
        .setStyle("min-height", "400px")
        .setStyle("background", "var(--dwc-surface-1)")
        .setStyle("border-radius", "var(--dwc-border-radius-m)")
        .setStyle("padding", "var(--dwc-space-l)");
    
    H3 cardTitle = new H3(title);
    cardTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0");
    
    chart.setStyle("width", "100%")
         .setStyle("height", "300px");
    
    card.add(cardTitle, chart);
    return card;
  }

  private GoogleChart createPieChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.PIE);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Cryptocurrency", "Market Cap"));
    data.add(Arrays.asList("Bitcoin", 45.2));
    data.add(Arrays.asList("Ethereum", 18.5));
    data.add(Arrays.asList("Binance Coin", 8.3));
    data.add(Arrays.asList("Cardano", 4.2));
    data.add(Arrays.asList("Others", 23.8));
    
    chart.setData(data);
    return chart;
  }

  private GoogleChart createLineChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.LINE);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Hour", "Volume (Billions)"));
    
    for (int i = 0; i < 24; i++) {
      double volume = 80 + Math.random() * 40;
      data.add(Arrays.asList(i + ":00", volume));
    }
    
    chart.setData(data);
    return chart;
  }

  private GoogleChart createColumnChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.COLUMN);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Crypto", "Performance %"));
    data.add(Arrays.asList("BTC", 12.5));
    data.add(Arrays.asList("ETH", 8.3));
    data.add(Arrays.asList("BNB", -2.1));
    data.add(Arrays.asList("ADA", 15.7));
    data.add(Arrays.asList("SOL", 22.4));
    data.add(Arrays.asList("DOT", -5.8));
    
    chart.setData(data);
    return chart;
  }

  private GoogleChart createAreaChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.AREA);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Day", "Bullish", "Bearish"));
    
    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    for (String day : days) {
      double bullish = 40 + Math.random() * 30;
      double bearish = 100 - bullish;
      data.add(Arrays.asList(day, bullish, bearish));
    }
    
    chart.setData(data);
    return chart;
  }
}