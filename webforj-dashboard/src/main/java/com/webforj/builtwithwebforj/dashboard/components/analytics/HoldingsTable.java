package com.webforj.builtwithwebforj.dashboard.components.analytics;

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
    // Major cryptocurrencies
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
    
    // Additional Layer 1 & 2 tokens
    holdings.add(new PortfolioItem("Binance Coin", 85, 245.30, 312.45));
    holdings.add(new PortfolioItem("XRP", 12000, 0.48, 0.62));
    holdings.add(new PortfolioItem("Litecoin", 45, 78.20, 91.35));
    holdings.add(new PortfolioItem("Algorand", 3500, 0.28, 0.31));
    holdings.add(new PortfolioItem("VeChain", 18000, 0.024, 0.029));
    holdings.add(new PortfolioItem("Tezos", 920, 4.85, 3.92));
    holdings.add(new PortfolioItem("Stellar", 6800, 0.089, 0.115));
    holdings.add(new PortfolioItem("TRON", 15500, 0.065, 0.078));
    holdings.add(new PortfolioItem("Hedera", 2400, 0.152, 0.167));
    holdings.add(new PortfolioItem("Internet Computer", 28, 42.50, 38.20));
    
    // DeFi tokens
    holdings.add(new PortfolioItem("Uniswap", 180, 18.75, 22.40));
    holdings.add(new PortfolioItem("Aave", 65, 125.80, 98.50));
    holdings.add(new PortfolioItem("Compound", 320, 185.20, 156.30));
    holdings.add(new PortfolioItem("SushiSwap", 850, 4.25, 3.80));
    holdings.add(new PortfolioItem("Curve DAO", 1200, 2.45, 2.89));
    holdings.add(new PortfolioItem("1inch", 2500, 1.85, 1.62));
    holdings.add(new PortfolioItem("PancakeSwap", 780, 8.90, 7.45));
    holdings.add(new PortfolioItem("Maker", 12, 1450.00, 1789.50));
    
    // Gaming & NFT tokens
    holdings.add(new PortfolioItem("Axie Infinity", 220, 35.60, 28.40));
    holdings.add(new PortfolioItem("The Sandbox", 1800, 2.85, 3.42));
    holdings.add(new PortfolioItem("Decentraland", 950, 3.20, 2.68));
    holdings.add(new PortfolioItem("Enjin Coin", 1400, 1.25, 1.48));
    holdings.add(new PortfolioItem("Gala", 4200, 0.185, 0.142));
    
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