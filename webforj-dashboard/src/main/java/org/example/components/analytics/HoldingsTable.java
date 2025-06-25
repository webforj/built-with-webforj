package org.example.components.analytics;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H3;
import com.webforj.component.table.Table;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

import java.util.ArrayList;
import java.util.List;

public class HoldingsTable extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private Table<PortfolioItem> portfolioTable;

  public HoldingsTable() {
    self.addClassName("analytics-view__table-solo")
        .setDirection(FlexDirection.COLUMN);

    portfolioTable = new Table<>();
    portfolioTable.addClassName("analytics-view__table");

    // Configure columns
    portfolioTable.addColumn("Asset", PortfolioItem::getAsset);
    portfolioTable.addColumn("Amount", item -> String.valueOf(item.getAmount()));
    portfolioTable.addColumn("Avg Buy Price", item -> String.format("$%.2f", item.getAvgBuyPrice()));
    portfolioTable.addColumn("Current Price", item -> String.format("$%.2f", item.getCurrentPrice()));
    portfolioTable.addColumn("Value", item -> String.format("$%.2f", item.getValue()));
    portfolioTable.addColumn("Profit/Loss",
        item -> String.format("$%.2f (%.1f%%)", item.getProfitLoss(), item.getProfitLossPercent()));
    portfolioTable.setItems(createSampleHoldings());
    self.add(portfolioTable);
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

    public String getAsset() {
      return asset;
    }

    public double getAmount() {
      return amount;
    }

    public double getAvgBuyPrice() {
      return avgBuyPrice;
    }

    public double getCurrentPrice() {
      return currentPrice;
    }

    public double getValue() {
      return amount * currentPrice;
    }

    public double getProfitLoss() {
      return (currentPrice - avgBuyPrice) * amount;
    }

    public double getProfitLossPercent() {
      return ((currentPrice - avgBuyPrice) / avgBuyPrice) * 100;
    }
  }
}