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
import org.example.utils.charts.ChartRedrawable;
import org.example.utils.charts.DashboardChartBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "analytics", outlet = MainLayout.class)
@StyleSheet("ws://analytics-view.css")
@FrameTitle("Analytics & Portfolio")
public class AnalyticsView extends Composite<FlexLayout> implements ChartRedrawable {
  private final FlexLayout self = getBoundComponent();
  private final DashboardChartBuilder chartBuilder = new DashboardChartBuilder();
  
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
    self.addClassName("analytics-view")
        .setDirection(FlexDirection.COLUMN);
    
    createHeader();
    self.add(new PortfolioHero());
    createMetricCards();
    createChartsSection();
    self.add(new HoldingsTable());
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.addClassName("analytics-view__header")
          .setJustifyContent(FlexJustifyContent.BETWEEN)
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
    addButton.setPrefixComponent(TablerIcon.create("plus"))
             .setTheme(ButtonTheme.PRIMARY);
    
    Button exportButton = new Button("Export");
    exportButton.setPrefixComponent(TablerIcon.create("download"));
    
    actions.add(addButton, exportButton);
    
    header.add(titleSection, actions);
    self.add(header);
  }

  private void createMetricCards() {
    FlexLayout cardsSection = new FlexLayout();
    cardsSection.addClassName("analytics-view__cards")
                .setJustifyContent(FlexJustifyContent.BETWEEN)
                .setWrap(FlexWrap.WRAP)
                .setSpacing("var(--dwc-space-m)");
    
    // Create dashboard cards using the new architecture
    totalVolumeCard = createAnalyticsCard("Total Market Volume", 152500000000.0, 12.5, GoogleChart.Type.AREA);
    portfolioValueCard = createAnalyticsCard("Portfolio Performance", 125480.50, 27.8, GoogleChart.Type.AREA);
    volatilityIndexCard = createAnalyticsCard("Market Volatility", 68.5, 8.7, GoogleChart.Type.LINE);
    
    cardsSection.add(totalVolumeCard, portfolioValueCard, volatilityIndexCard);
    self.add(cardsSection);
  }

  private void createChartsSection() {
    FlexLayout chartsContainer = new FlexLayout();
    chartsContainer.addClassName("analytics-view__charts")
                   .setWrap(FlexWrap.WRAP)
                   .setSpacing("var(--dwc-space-l)");
    
    allocationChart = new ChartCard("Portfolio Allocation", createPortfolioAllocationChart());
    volumeChart = new ChartCard("24h Trading Volume", createLineChart());
    performanceChart = new ChartCard("Top Assets Performance (7d)", createColumnChart());
    sentimentChart = new ChartCard("Market Sentiment Analysis", createAreaChart(), true);
    
    chartsContainer.add(allocationChart, volumeChart, performanceChart, sentimentChart);
    self.add(chartsContainer);
  }

  private GoogleChart createPortfolioAllocationChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.PIE);
    
    List<Object> data = Arrays.asList(
        Arrays.asList("Asset", "Value"),
        Arrays.asList("Bitcoin", 45250.00),
        Arrays.asList("Ethereum", 32100.00),
        Arrays.asList("Cardano", 18500.00),
        Arrays.asList("Polkadot", 12800.00),
        Arrays.asList("Solana", 16830.50)
    );
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
    
    List<Object> data = Arrays.asList(
        Arrays.asList("Crypto", "Performance %"),
        Arrays.asList("BTC", 12.5),
        Arrays.asList("ETH", 8.3),
        Arrays.asList("BNB", -2.1),
        Arrays.asList("ADA", 15.7),
        Arrays.asList("SOL", 22.4),
        Arrays.asList("DOT", -5.8)
    );
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
  
  /**
   * Creates a dashboard card for analytics using the new architecture.
   * 
   * @param title The card title
   * @param value The card value
   * @param percentage The percentage change
   * @param chartType The type of chart to create
   * @return A configured DashboardCard
   */
  private DashboardCard createAnalyticsCard(String title, double value, double percentage, GoogleChart.Type chartType) {
    // Build the chart using the chart builder
    GoogleChart chart = chartBuilder.buildDashboardChart(chartType, percentage);
    
    // Create the card with data and chart
    return new DashboardCard(title, value, percentage, chart);
  }
  
  @Override
  public void redrawCharts() {
    // Using the default interface methods
    redrawDashboardCards(totalVolumeCard, portfolioValueCard, volatilityIndexCard);
    redrawChartCards(allocationChart, volumeChart, performanceChart, sentimentChart);
  }

}