package org.example.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Table;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import org.example.components.DashboardCard;
import org.example.components.PortfolioHero;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "analytics", outlet = MainLayout.class)
@StyleSheet("ws://analytics-view.css")
@FrameTitle("Analytics & Portfolio")
public class AnalyticsView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private Table<PortfolioItem> portfolioTable;

  public AnalyticsView() {
    self.addClassName("analytics-view");
    self.setDirection(FlexDirection.COLUMN);
    
    // Create header
    createHeader();
    
    // Create portfolio hero section
    PortfolioHero portfolioHero = new PortfolioHero();
    self.add(portfolioHero);
    
    // Create metric cards
    createMetricCards();
    
    // Create charts section
    createChartsSection();
    
    // Create holdings table
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
    cardsSection.addClassName("analytics-view__cards-section");
    cardsSection.setJustifyContent(FlexJustifyContent.BETWEEN)
                .setWrap(FlexWrap.WRAP);
    
    DashboardCard totalVolume = new DashboardCard("Total Market Volume", 152500000000.0, 12.5);
    DashboardCard portfolioValue = new DashboardCard("Portfolio Performance", 125480.50, 27.8, GoogleChart.Type.AREA);
    DashboardCard volatilityIndex = new DashboardCard("Market Volatility", 68.5, 8.7, GoogleChart.Type.LINE);
    
    cardsSection.add(totalVolume, portfolioValue, volatilityIndex);
    self.add(cardsSection);
  }

  private void createChartsSection() {
    FlexLayout chartsContainer = new FlexLayout();
    chartsContainer.addClassName("analytics-view__charts-container");
    chartsContainer.setWrap(FlexWrap.WRAP);
    
    // Portfolio Allocation Chart
    Div allocationChart = createChartCard("Portfolio Allocation", createPortfolioAllocationChart());
    
    // Trading Volume History Chart
    Div volumeChart = createChartCard("24h Trading Volume", createLineChart());
    
    // Asset Performance Chart
    Div performanceChart = createChartCard("Top Assets Performance (7d)", createColumnChart());
    
    // Market Sentiment Chart (full width)
    Div sentimentChart = createFullWidthChartCard("Market Sentiment Analysis", createAreaChart());
    
    chartsContainer.add(allocationChart, volumeChart, performanceChart, sentimentChart);
    self.add(chartsContainer);
  }

  private Div createChartCard(String title, GoogleChart chart) {
    Div card = new Div();
    card.addClassName("analytics-view__chart-card");
    
    H3 cardTitle = new H3(title);
    cardTitle.addClassName("analytics-view__chart-title");
    
    chart.addClassName("analytics-view__chart");
    
    card.add(cardTitle, chart);
    return card;
  }

  private Div createFullWidthChartCard(String title, GoogleChart chart) {
    Div card = new Div();
    card.addClassName("analytics-view__chart-card");
    card.addClassName("analytics-view__chart-card--full-width");
    
    H3 cardTitle = new H3(title);
    cardTitle.addClassName("analytics-view__chart-title");
    
    chart.addClassName("analytics-view__chart");
    chart.addClassName("analytics-view__chart--full-width");
    
    card.add(cardTitle, chart);
    return card;
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
    
    // Configure theme-aware options
    Map<String, Object> options = new HashMap<>();
    options.put("backgroundColor", "transparent");
    options.put("pieSliceTextStyle", Map.of("color", "white"));
    options.put("legend", Map.of(
        "textStyle", Map.of("color", "#6b7280")
    ));
    options.put("chartArea", Map.of(
        "left", "10%",
        "top", "10%",
        "width", "80%",
        "height", "75%"
    ));
    chart.setOptions(options);
    
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
    
    // Configure theme-aware options
    Map<String, Object> options = new HashMap<>();
    options.put("backgroundColor", "transparent");
    options.put("colors", List.of("#3b82f6"));
    options.put("legend", "none");
    options.put("hAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280"),
        "gridlines", Map.of("color", "#e5e7eb")
    ));
    options.put("vAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280"),
        "gridlines", Map.of("color", "#e5e7eb")
    ));
    options.put("chartArea", Map.of(
        "left", "10%",
        "top", "10%",
        "width", "80%",
        "height", "75%"
    ));
    chart.setOptions(options);
    
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
    
    // Configure theme-aware options
    Map<String, Object> options = new HashMap<>();
    options.put("backgroundColor", "transparent");
    options.put("colors", List.of("#10b981"));
    options.put("legend", "none");
    options.put("hAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280")
    ));
    options.put("vAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280"),
        "gridlines", Map.of("color", "#e5e7eb")
    ));
    options.put("chartArea", Map.of(
        "left", "10%",
        "top", "10%",
        "width", "80%",
        "height", "75%"
    ));
    chart.setOptions(options);
    
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
    
    // Configure theme-aware options
    Map<String, Object> options = new HashMap<>();
    options.put("backgroundColor", "transparent");
    options.put("colors", List.of("#10b981", "#ef4444"));
    options.put("isStacked", true);
    options.put("hAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280")
    ));
    options.put("vAxis", Map.of(
        "textStyle", Map.of("color", "#6b7280"),
        "gridlines", Map.of("color", "#e5e7eb")
    ));
    options.put("legend", Map.of(
        "textStyle", Map.of("color", "#6b7280")
    ));
    options.put("chartArea", Map.of(
        "left", "10%",
        "top", "10%",
        "width", "80%",
        "height", "70%"
    ));
    chart.setOptions(options);
    
    return chart;
  }
  
  private void createHoldingsTable() {
    Div tableSection = new Div();
    tableSection.addClassName("analytics-view__holdings-table-section");
    
    H3 tableTitle = new H3("Portfolio Holdings");
    tableTitle.addClassName("analytics-view__table-title");
    
    portfolioTable = new Table<>();
    portfolioTable.addClassName("analytics-view__table");
    
    // Configure columns
    portfolioTable.addColumn("Asset", PortfolioItem::getAsset);
    portfolioTable.addColumn("Amount", item -> String.valueOf(item.getAmount()));
    portfolioTable.addColumn("Avg Buy Price", item -> String.format("$%.2f", item.getAvgBuyPrice()));
    portfolioTable.addColumn("Current Price", item -> String.format("$%.2f", item.getCurrentPrice()));
    portfolioTable.addColumn("Value", item -> String.format("$%.2f", item.getValue()));
    portfolioTable.addColumn("Profit/Loss", item -> {
      double pl = item.getProfitLoss();
      String formatted = String.format("$%.2f (%.1f%%)", pl, item.getProfitLossPercent());
      return formatted;
    });
    
    // Add sample data
    List<PortfolioItem> holdings = createSampleHoldings();
    portfolioTable.setItems(holdings);
    
    tableSection.add(tableTitle, portfolioTable);
    self.add(tableSection);
  }

  private List<PortfolioItem> createSampleHoldings() {
    List<PortfolioItem> holdings = new ArrayList<>();
    holdings.add(new PortfolioItem("Bitcoin", 1.25, 28500, 36200));
    holdings.add(new PortfolioItem("Ethereum", 15.5, 1850, 2071));
    holdings.add(new PortfolioItem("Cardano", 25000, 0.65, 0.74));
    holdings.add(new PortfolioItem("Polkadot", 800, 18.50, 16.00));
    holdings.add(new PortfolioItem("Solana", 150, 95.20, 112.20));
    holdings.add(new PortfolioItem("Chainlink", 450, 22.80, 26.45));
    holdings.add(new PortfolioItem("Polygon", 8500, 0.85, 1.12));
    holdings.add(new PortfolioItem("Avalanche", 200, 65.40, 42.30));
    holdings.add(new PortfolioItem("Cosmos", 1200, 18.20, 25.60));
    holdings.add(new PortfolioItem("Near Protocol", 650, 12.50, 8.90));
    return holdings;
  }

  // Inner class for portfolio items
  public static class PortfolioItem {
    private final String asset;
    private final double amount;
    private final double avgBuyPrice;
    private final double currentPrice;

    public PortfolioItem(String asset, double amount, double avgBuyPrice, double currentPrice) {
      this.asset = asset;
      this.amount = amount;
      this.avgBuyPrice = avgBuyPrice;
      this.currentPrice = currentPrice;
    }

    public String getAsset() { return asset; }
    public double getAmount() { return amount; }
    public double getAvgBuyPrice() { return avgBuyPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public double getValue() { return amount * currentPrice; }
    public double getProfitLoss() { return (currentPrice - avgBuyPrice) * amount; }
    public double getProfitLossPercent() { return ((currentPrice - avgBuyPrice) / avgBuyPrice) * 100; }
  }
}