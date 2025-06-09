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
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Table;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "portfolio", outlet = MainLayout.class)
@StyleSheet("ws://portfolio-view.css")
@FrameTitle("Portfolio")
public class PortfolioView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private Table<PortfolioItem> portfolioTable;

  public PortfolioView() {
    self.addClassName("portfolio-view");
    self.setDirection(FlexDirection.COLUMN);
    
    // Create header
    createHeader();
    
    // Create portfolio hero section
    createPortfolioHero();
    
    // Create portfolio allocation chart
    createAllocationChart();
    
    // Create holdings table
    createHoldingsTable();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.addClassName("portfolio-view__header");
    header.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setWrap(FlexWrap.WRAP);
    
    FlexLayout titleSection = new FlexLayout();
    titleSection.setDirection(FlexDirection.COLUMN);
    
    H2 title = new H2("My Portfolio");
    title.addClassName("portfolio-view__title");
    
    Paragraph description = new Paragraph("Track and manage your cryptocurrency investments");
    description.addClassName("portfolio-view__description");
    
    titleSection.add(title, description);
    
    // Action buttons
    FlexLayout actions = new FlexLayout();
    actions.addClassName("portfolio-view__actions");
    
    Button addButton = new Button("Add Asset");
    addButton.setPrefixComponent(TablerIcon.create("plus"));
    addButton.setTheme(ButtonTheme.PRIMARY);
    
    Button exportButton = new Button("Export");
    exportButton.setPrefixComponent(TablerIcon.create("download"));
    
    actions.add(addButton, exportButton);
    
    header.add(titleSection, actions);
    self.add(header);
  }

  private void createPortfolioHero() {
    Div heroSection = new Div();
    heroSection.addClassName("portfolio-view__hero");
    
    // Main content area with metrics
    FlexLayout heroContent = new FlexLayout();
    heroContent.addClassName("portfolio-view__hero-content");
    
    // Left side - Main portfolio value
    FlexLayout mainValue = new FlexLayout();
    mainValue.addClassName("portfolio-view__hero-main");
    mainValue.setDirection(FlexDirection.COLUMN);
    
    Paragraph welcomeText = new Paragraph("Portfolio Value");
    welcomeText.addClassName("portfolio-view__hero-label");
    
    H2 totalValue = new H2("$125,480.50");
    totalValue.addClassName("portfolio-view__hero-value");
    
    FlexLayout changeContainer = new FlexLayout();
    changeContainer.addClassName("portfolio-view__hero-change");
    
    Span changeValue = new Span("+ $27,280.50");
    changeValue.addClassName("portfolio-view__hero-change-value");
    
    Span changePercent = new Span("(+27.8%)");
    changePercent.addClassName("portfolio-view__hero-change-percent");
    
    changeContainer.add(changeValue, changePercent);
    
    mainValue.add(welcomeText, totalValue, changeContainer);
    
    // Right side - Quick stats grid
    FlexLayout statsGrid = new FlexLayout();
    statsGrid.addClassName("portfolio-view__hero-stats");
    statsGrid.setDirection(FlexDirection.COLUMN);
    
    statsGrid.add(
      createQuickStat("24h Change", "+2.8%", "trending-up"),
      createQuickStat("Total Assets", "20", "coins"),
      createQuickStat("Best Performer", "SOL +22.4%", "trophy")
    );
    
    heroContent.add(mainValue, statsGrid);
    heroSection.add(heroContent);
    
    self.add(heroSection);
  }

  private FlexLayout createQuickStat(String label, String value, String iconName) {
    FlexLayout stat = new FlexLayout();
    stat.addClassName("portfolio-view__quick-stat");
    
    Div iconContainer = new Div();
    iconContainer.addClassName("portfolio-view__quick-stat-icon");
    iconContainer.setHtml("<dwc-icon name=\"tabler:" + iconName + "\"></dwc-icon>");
    
    FlexLayout textContainer = new FlexLayout();
    textContainer.addClassName("portfolio-view__quick-stat-text");
    textContainer.setDirection(FlexDirection.COLUMN);
    
    Paragraph statLabel = new Paragraph(label);
    statLabel.addClassName("portfolio-view__quick-stat-label");
    
    Span statValue = new Span(value);
    statValue.addClassName("portfolio-view__quick-stat-value");
    
    textContainer.add(statLabel, statValue);
    stat.add(iconContainer, textContainer);
    
    return stat;
  }

  private void createAllocationChart() {
    FlexLayout chartSection = new FlexLayout();
    chartSection.addClassName("portfolio-view__chart-section");
    chartSection.setWrap(FlexWrap.WRAP);
    
    // Pie chart
    Div pieChartCard = new Div();
    pieChartCard.addClassName("portfolio-view__allocation-chart-card");
    
    H3 chartTitle = new H3("Portfolio Allocation");
    chartTitle.addClassName("portfolio-view__chart-title");
    
    GoogleChart pieChart = new GoogleChart(GoogleChart.Type.PIE);
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Asset", "Value"));
    data.add(Arrays.asList("Bitcoin", 45250.00));
    data.add(Arrays.asList("Ethereum", 32100.00));
    data.add(Arrays.asList("Cardano", 18500.00));
    data.add(Arrays.asList("Polkadot", 12800.00));
    data.add(Arrays.asList("Solana", 16830.50));
    
    pieChart.setData(data);
    pieChart.addClassName("portfolio-view__chart");
    
    pieChartCard.add(chartTitle, pieChart);
    
    // Performance chart
    Div performanceCard = new Div();
    performanceCard.addClassName("portfolio-view__performance-chart-card");
    
    H3 perfTitle = new H3("Portfolio Performance (30d)");
    perfTitle.addClassName("portfolio-view__chart-title");
    
    GoogleChart lineChart = new GoogleChart(GoogleChart.Type.LINE);
    List<Object> lineData = new ArrayList<>();
    lineData.add(Arrays.asList("Day", "Portfolio Value"));
    
    double baseValue = 110000;
    for (int i = 0; i < 30; i++) {
      baseValue += (Math.random() - 0.3) * 2000;
      lineData.add(Arrays.asList("Day " + (i + 1), baseValue));
    }
    
    lineChart.setData(lineData);
    lineChart.addClassName("portfolio-view__chart");
    
    performanceCard.add(perfTitle, lineChart);
    
    chartSection.add(pieChartCard, performanceCard);
    self.add(chartSection);
  }

  private void createHoldingsTable() {
    Div tableSection = new Div();
    tableSection.addClassName("portfolio-view__holdings-table-section");
    
    H3 tableTitle = new H3("Holdings");
    tableTitle.addClassName("portfolio-view__table-title");
    
    portfolioTable = new Table<>();
    portfolioTable.addClassName("portfolio-view__table");
    
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
    holdings.add(new PortfolioItem("Litecoin", 85, 75.20, 92.15));
    holdings.add(new PortfolioItem("Uniswap", 1200, 5.80, 7.25));
    holdings.add(new PortfolioItem("VeChain", 45000, 0.032, 0.045));
    holdings.add(new PortfolioItem("Algorand", 3500, 0.28, 0.31));
    holdings.add(new PortfolioItem("Stellar", 8200, 0.095, 0.118));
    holdings.add(new PortfolioItem("Tezos", 750, 1.85, 1.12));
    holdings.add(new PortfolioItem("The Graph", 2800, 0.145, 0.185));
    holdings.add(new PortfolioItem("Filecoin", 120, 8.50, 6.80));
    holdings.add(new PortfolioItem("Hedera", 15000, 0.068, 0.095));
    holdings.add(new PortfolioItem("Internet Computer", 45, 12.40, 8.90));
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