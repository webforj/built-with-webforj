package org.example.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;


import org.example.components.analytics.ChartCard;
import org.example.components.analytics.HoldingsTable;
import org.example.components.analytics.PortfolioHero;
import org.example.components.dashboard.DashboardCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "analytics", outlet = MainLayout.class)
@StyleSheet("ws://analytics-view.css")
@FrameTitle("Analytics & Portfolio")
public class AnalyticsView extends Composite<FlexLayout> implements org.example.utils.ChartRedrawable {
  private final FlexLayout self = getBoundComponent();
  private HoldingsTable holdingsTable;
  
  // Store references to dashboard cards with charts
  private DashboardCard totalVolumeCard;
  private DashboardCard portfolioValueCard;
  private DashboardCard volatilityIndexCard;
  
  // Store references to chart cards
  private ChartCard allocationChart;
  private ChartCard volumeChart;
  private ChartCard performanceChart;
  private ChartCard sentimentChart;

  public AnalyticsView() {
    self.addClassName("analytics-view");
    self.setDirection(FlexDirection.COLUMN);
    
    createHeader();
    self.add(new PortfolioHero());
    createMetricCards();
    createChartsSection();
    createHoldingsTable();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.addClassName("analytics-view__header");
    header.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setWrap(FlexWrap.WRAP);
    
    FlexLayout titleSection = new FlexLayout();
    titleSection.setDirection(FlexDirection.COLUMN);
    
    H2 title = new H2("Analytics & Portfolio");
    title.addClassName("analytics-view__title");
    
    Paragraph description = new Paragraph("Comprehensive market analysis and portfolio management");
    description.addClassName("analytics-view__description");
    
    titleSection.add(title, description);
    
    // Action buttons
    FlexLayout actions = new FlexLayout();
    actions.addClassName("analytics-view__actions");
    
    Button addButton = new Button("Add Asset");
    addButton.setPrefixComponent(TablerIcon.create("plus"));
    addButton.setTheme(ButtonTheme.PRIMARY);
    
    Button exportButton = new Button("Export");
    exportButton.setPrefixComponent(TablerIcon.create("download"));
    
    actions.add(addButton, exportButton);
    
    header.add(titleSection, actions);
    self.add(header);
  }

  private void createMetricCards() {
    FlexLayout cardsSection = new FlexLayout();
    cardsSection.addClassName("analytics-view__cards");
    cardsSection.setJustifyContent(FlexJustifyContent.BETWEEN)
                .setWrap(FlexWrap.WRAP);
    
    totalVolumeCard = new DashboardCard("Total Market Volume", 152500000000.0, 12.5);
    portfolioValueCard = new DashboardCard("Portfolio Performance", 125480.50, 27.8, GoogleChart.Type.AREA);
    volatilityIndexCard = new DashboardCard("Market Volatility", 68.5, 8.7, GoogleChart.Type.LINE);
    
    cardsSection.add(totalVolumeCard, portfolioValueCard, volatilityIndexCard);
    self.add(cardsSection);
  }

  private void createChartsSection() {
    FlexLayout chartsContainer = new FlexLayout();
    chartsContainer.addClassName("analytics-view__charts");
    chartsContainer.setWrap(FlexWrap.WRAP);
    
    allocationChart = new ChartCard("Portfolio Allocation", createPortfolioAllocationChart());
    volumeChart = new ChartCard("24h Trading Volume", createLineChart());
    performanceChart = new ChartCard("Top Assets Performance (7d)", createColumnChart());
    sentimentChart = new ChartCard("Market Sentiment Analysis", createAreaChart(), true);
    
    chartsContainer.add(allocationChart, volumeChart, performanceChart, sentimentChart);
    self.add(chartsContainer);
  }

  private GoogleChart createPortfolioAllocationChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.PIE);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Asset", "Value"));
    data.add(Arrays.asList("Bitcoin", 45250.00));
    data.add(Arrays.asList("Ethereum", 32100.00));
    data.add(Arrays.asList("Cardano", 18500.00));
    data.add(Arrays.asList("Polkadot", 12800.00));
    data.add(Arrays.asList("Solana", 16830.50));
    chart.setData(data);
    
    Map<String, Object> options = new HashMap<>();
    options.put("backgroundColor", "transparent");
    options.put("pieSliceTextStyle", Map.of("color", "white"));
    options.put("legend", Map.of("textStyle", Map.of("color", "#6b7280")));
    options.put("chartArea", Map.of("left", "10%", "top", "10%", "width", "80%", "height", "75%"));
    chart.setOptions(options);
    
    return chart;
  }

  private GoogleChart createLineChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.LINE);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Hour", "Volume (Billions)"));
    
    for (int i = 0; i < 24; i++) {
      data.add(Arrays.asList(i + ":00", 80 + Math.random() * 40));
    }
    
    chart.setData(data);
    chart.setOptions(createStandardChartOptions("#3b82f6"));
    
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
    chart.setOptions(createStandardChartOptions("#10b981"));
    
    return chart;
  }

  private GoogleChart createAreaChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.AREA);
    
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Day", "Bullish", "Bearish"));
    
    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    for (String day : days) {
      double bullish = 40 + Math.random() * 30;
      data.add(Arrays.asList(day, bullish, 100 - bullish));
    }
    
    chart.setData(data);
    
    Map<String, Object> options = createStandardChartOptions(null);
    options.put("colors", List.of("#10b981", "#ef4444"));
    options.put("isStacked", true);
    options.put("legend", Map.of("textStyle", Map.of("color", "#6b7280")));
    options.put("chartArea", Map.of("left", "10%", "top", "10%", "width", "80%", "height", "70%"));
    chart.setOptions(options);
    
    return chart;
  }
  
  private Map<String, Object> createStandardChartOptions(String color) {
    Map<String, Object> options = new HashMap<>();
    options.put("backgroundColor", "transparent");
    if (color != null) {
      options.put("colors", List.of(color));
    }
    options.put("legend", "none");
    options.put("hAxis", Map.of("textStyle", Map.of("color", "#6b7280"), "gridlines", Map.of("color", "#e5e7eb")));
    options.put("vAxis", Map.of("textStyle", Map.of("color", "#6b7280"), "gridlines", Map.of("color", "#e5e7eb")));
    options.put("chartArea", Map.of("left", "10%", "top", "10%", "width", "80%", "height", "75%"));
    return options;
  }
  
  private void createHoldingsTable() {
    holdingsTable = new HoldingsTable();
    self.add(holdingsTable);
  }
  
  @Override
  public void redrawCharts() {
    // Using the default interface methods
    redrawDashboardCards(totalVolumeCard, portfolioValueCard, volatilityIndexCard);
    redrawChartCards(allocationChart, volumeChart, performanceChart, sentimentChart);
  }

}