package com.webforj.builtwithwebforj.dashboard.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.dashboard.components.analytics.ChartCard;
import com.webforj.builtwithwebforj.dashboard.components.analytics.HoldingsTable;
import com.webforj.builtwithwebforj.dashboard.components.analytics.PortfolioHero;
import com.webforj.builtwithwebforj.dashboard.components.dashboard.DashboardCard;
import com.webforj.builtwithwebforj.dashboard.utils.charts.ChartRedrawable;
import com.webforj.builtwithwebforj.dashboard.utils.charts.DashboardChartBuilder;
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

  // Store references to chart cards
  private ChartCard allocationChart;
  private ChartCard volumeChart;
  private ChartCard performanceChart;
  private ChartCard sentimentChart;
  
  // Store references to dashboard cards
  private DashboardCard marketCapCard;
  private DashboardCard totalVolumeCard;
  private DashboardCard fearGreedCard;
  
  private final DashboardChartBuilder chartBuilder = new DashboardChartBuilder();

  public AnalyticsView() {
    self.addClassName("analytics-view")
        .setDirection(FlexDirection.COLUMN);

    createHeader();
    self.add(new PortfolioHero());
    createDashboardCardsSection();
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
        Arrays.asList("Solana", 16830.50));
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
        Arrays.asList("DOT", -5.8));
    chart.setData(data);
    chart.setOptions(createStandardChartOptions("#10b981"));

    return chart;
  }

  private GoogleChart createAreaChart() {
    GoogleChart chart = new GoogleChart(GoogleChart.Type.AREA);

    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Time", "Bullish", "Bearish", "Neutral"));

    // Create 48 data points (every 30 minutes for 24 hours)
    double previousBullish = 45;
    double previousNeutral = 15;
    
    for (int i = 0; i < 48; i++) {
      String timeLabel;
      if (i % 4 == 0) { // Show hour labels every 2 hours
        int hour = (i / 2) % 24;
        timeLabel = String.format("%02d:00", hour);
      } else {
        timeLabel = "";
      }
      
      // Create more realistic sentiment fluctuations with momentum
      double momentum = (Math.random() - 0.5) * 8;
      double volatility = Math.sin(i * 0.3) * 5; // Add cyclical pattern
      
      double bullish = Math.max(20, Math.min(65, previousBullish + momentum + volatility));
      double neutral = Math.max(10, Math.min(30, previousNeutral + (Math.random() - 0.5) * 3));
      double bearish = 100 - bullish - neutral;
      
      data.add(Arrays.asList(timeLabel, bullish, bearish, neutral));
      
      previousBullish = bullish;
      previousNeutral = neutral;
    }

    chart.setData(data);

    Map<String, Object> options = createStandardChartOptions(null);
    options.put("colors", List.of("#10b981", "#ef4444", "#6b7280"));
    options.put("isStacked", true);
    options.put("areaOpacity", 0.8);
    options.put("legend", Map.of(
        "position", "top",
        "textStyle", Map.of("color", "#6b7280", "fontSize", 12)
    ));
    options.put("hAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280", "fontSize", 10),
        "gridlines", Map.of("color", "#e5e7eb"),
        "showTextEvery", 4,
        "slantedText", false
    ));
    options.put("vAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280"),
        "gridlines", Map.of("color", "#e5e7eb"),
        "format", "#'%'"
    ));
    options.put("chartArea", Map.of("left", "10%", "top", "15%", "width", "85%", "height", "65%"));
    options.put("curveType", "function");
    options.put("animation", Map.of("startup", true, "duration", 1000));
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


  private void createDashboardCardsSection() {
    FlexLayout dashboardCards = new FlexLayout();
    dashboardCards.addClassName("analytics-view__dashboard-cards")
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-l)");

    // Create dashboard cards with market insights
    marketCapCard = new DashboardCard("Total Market Cap", 2420000000000.0, 2.85, 
        chartBuilder.buildDashboardChart(GoogleChart.Type.AREA, 2.85));
    
    totalVolumeCard = new DashboardCard("24h Volume", 89400000000.0, -1.24, 
        chartBuilder.buildDashboardChart(GoogleChart.Type.COLUMN, -1.24));
    
    fearGreedCard = new DashboardCard("Fear & Greed Index", 68.0, 4.12, 
        chartBuilder.buildDashboardChart(GoogleChart.Type.LINE, 4.12));

    dashboardCards.add(marketCapCard, totalVolumeCard, fearGreedCard);
    self.add(dashboardCards);
  }

  @Override
  public void redrawCharts() {
    // Using the default interface methods
    redrawChartCards(allocationChart, volumeChart, performanceChart, sentimentChart);
    redrawDashboardCards(marketCapCard, totalVolumeCard, fearGreedCard);
  }

}