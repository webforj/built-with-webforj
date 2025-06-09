package org.example.views;

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
@FrameTitle("Portfolio")
public class PortfolioView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private Table<PortfolioItem> portfolioTable;

  public PortfolioView() {
    self.addClassName("portfolio-view");
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("overflow-y", "auto");
    self.setStyle("padding", "var(--dwc-space-l)");
    
    // Create header
    createHeader();
    
    // Create portfolio summary
    createPortfolioSummary();
    
    // Create portfolio allocation chart
    createAllocationChart();
    
    // Create holdings table
    createHoldingsTable();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setStyle("margin-bottom", "var(--dwc-space-xl)")
          .setWrap(FlexWrap.WRAP)
          .setStyle("gap", "var(--dwc-space-m)");
    
    FlexLayout titleSection = new FlexLayout();
    titleSection.setDirection(FlexDirection.COLUMN);
    
    H2 title = new H2("My Portfolio");
    title.setStyle("margin", "0");
    
    Paragraph description = new Paragraph("Track and manage your cryptocurrency investments");
    description.setStyle("color", "var(--dwc-color-default-color)");
    description.setStyle("margin", "var(--dwc-space-s) 0 0 0");
    
    titleSection.add(title, description);
    
    // Action buttons
    FlexLayout actions = new FlexLayout();
    actions.setStyle("gap", "var(--dwc-space-m)");
    
    Button addButton = new Button("Add Asset");
    addButton.setPrefixComponent(TablerIcon.create("plus"));
    addButton.setTheme(ButtonTheme.PRIMARY);
    
    Button exportButton = new Button("Export");
    exportButton.setPrefixComponent(TablerIcon.create("download"));
    
    actions.add(addButton, exportButton);
    
    header.add(titleSection, actions);
    self.add(header);
  }

  private void createPortfolioSummary() {
    FlexLayout summaryCards = new FlexLayout();
    summaryCards.setJustifyContent(FlexJustifyContent.BETWEEN)
                .setWrap(FlexWrap.WRAP)
                .setStyle("gap", "var(--dwc-space-m)")
                .setStyle("margin-bottom", "var(--dwc-space-xl)");
    
    summaryCards.add(
      createSummaryCard("Total Value", "$125,480.50", "+12.3%", true),
      createSummaryCard("Total Invested", "$98,200.00", "", false),
      createSummaryCard("Total Profit", "$27,280.50", "+27.8%", true),
      createSummaryCard("24h Change", "$3,420.15", "+2.8%", true)
    );
    
    self.add(summaryCards);
  }

  private Div createSummaryCard(String label, String value, String change, boolean showChange) {
    Div card = new Div();
    card.addClassName("portfolio-summary-card");
    card.setStyle("flex", "1 1 200px")
        .setStyle("background", "var(--dwc-surface-1)")
        .setStyle("border-radius", "var(--dwc-border-radius-m)")
        .setStyle("padding", "var(--dwc-space-l)")
        .setStyle("position", "relative");
    
    FlexLayout content = new FlexLayout();
    content.setDirection(FlexDirection.COLUMN);
    
    Paragraph labelText = new Paragraph(label);
    labelText.setStyle("margin", "0")
             .setStyle("color", "var(--dwc-color-default-color)")
             .setStyle("font-size", "var(--dwc-font-size-s)");
    
    H3 valueText = new H3(value);
    valueText.setStyle("margin", "var(--dwc-space-s) 0 0 0");
    
    content.add(labelText, valueText);
    
    if (showChange && !change.isEmpty()) {
      Paragraph changeText = new Paragraph(change);
      changeText.setStyle("margin", "var(--dwc-space-xs) 0 0 0")
                .setStyle("font-size", "var(--dwc-font-size-s)")
                .setStyle("font-weight", "var(--dwc-font-weight-semibold)")
                .setStyle("color", change.startsWith("+") ? "var(--dwc-color-success-25)" : "var(--dwc-color-danger-40)");
      content.add(changeText);
    }
    
    // Add action buttons at bottom right
    FlexLayout buttonGroup = new FlexLayout();
    buttonGroup.setStyle("position", "absolute")
               .setStyle("bottom", "var(--dwc-space-m)")
               .setStyle("right", "var(--dwc-space-m)")
               .setStyle("gap", "var(--dwc-space-xs)");
    
    IconButton bellButton = new IconButton(TablerIcon.create("bell"));
    bellButton.setStyle("padding", "var(--dwc-space-xs)")
              .setStyle("background", "var(--dwc-surface-2)")
              .setStyle("border-radius", "var(--dwc-border-radius-s)")
              .setTooltipText("Set Alert");
    
    IconButton shareButton = new IconButton(TablerIcon.create("share"));
    shareButton.setStyle("padding", "var(--dwc-space-xs)")
               .setStyle("background", "var(--dwc-surface-2)")
               .setStyle("border-radius", "var(--dwc-border-radius-s)")
               .setTooltipText("Share");
    
    buttonGroup.add(bellButton, shareButton);
    
    card.add(content, buttonGroup);
    return card;
  }

  private void createAllocationChart() {
    FlexLayout chartSection = new FlexLayout();
    chartSection.setStyle("margin-bottom", "var(--dwc-space-xl)")
                .setWrap(FlexWrap.WRAP)
                .setStyle("gap", "var(--dwc-space-l)");
    
    // Pie chart
    Div pieChartCard = new Div();
    pieChartCard.addClassName("allocation-chart-card");
    pieChartCard.setStyle("flex", "1 1 400px")
                .setStyle("background", "var(--dwc-surface-1)")
                .setStyle("border-radius", "var(--dwc-border-radius-m)")
                .setStyle("padding", "var(--dwc-space-l)");
    
    H3 chartTitle = new H3("Portfolio Allocation");
    chartTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0");
    
    GoogleChart pieChart = new GoogleChart(GoogleChart.Type.PIE);
    List<Object> data = new ArrayList<>();
    data.add(Arrays.asList("Asset", "Value"));
    data.add(Arrays.asList("Bitcoin", 45250.00));
    data.add(Arrays.asList("Ethereum", 32100.00));
    data.add(Arrays.asList("Cardano", 18500.00));
    data.add(Arrays.asList("Polkadot", 12800.00));
    data.add(Arrays.asList("Solana", 16830.50));
    
    pieChart.setData(data);
    pieChart.setStyle("width", "100%")
            .setStyle("height", "300px");
    
    pieChartCard.add(chartTitle, pieChart);
    
    // Performance chart
    Div performanceCard = new Div();
    performanceCard.addClassName("performance-chart-card");
    performanceCard.setStyle("flex", "1 1 400px")
                   .setStyle("background", "var(--dwc-surface-1)")
                   .setStyle("border-radius", "var(--dwc-border-radius-m)")
                   .setStyle("padding", "var(--dwc-space-l)");
    
    H3 perfTitle = new H3("Portfolio Performance (30d)");
    perfTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0");
    
    GoogleChart lineChart = new GoogleChart(GoogleChart.Type.LINE);
    List<Object> lineData = new ArrayList<>();
    lineData.add(Arrays.asList("Day", "Portfolio Value"));
    
    double baseValue = 110000;
    for (int i = 0; i < 30; i++) {
      baseValue += (Math.random() - 0.3) * 2000;
      lineData.add(Arrays.asList("Day " + (i + 1), baseValue));
    }
    
    lineChart.setData(lineData);
    lineChart.setStyle("width", "100%")
             .setStyle("height", "300px");
    
    performanceCard.add(perfTitle, lineChart);
    
    chartSection.add(pieChartCard, performanceCard);
    self.add(chartSection);
  }

  private void createHoldingsTable() {
    Div tableSection = new Div();
    tableSection.addClassName("holdings-table-section");
    tableSection.setStyle("background", "var(--dwc-surface-1)")
                .setStyle("border-radius", "var(--dwc-border-radius-m)")
                .setStyle("padding", "var(--dwc-space-l)")
                .setStyle("overflow", "auto")
                .setStyle("max-height", "600px");
    
    H3 tableTitle = new H3("Holdings");
    tableTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0");
    
    portfolioTable = new Table<>();
    portfolioTable.addClassName("portfolio-table");
    
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