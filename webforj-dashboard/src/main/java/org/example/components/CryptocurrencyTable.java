package org.example.components;

import org.example.models.Cryptocurrency;
import org.example.utils.CryptoIconRenderer;
import org.example.utils.PriceChangeRenderer;
import org.example.utils.PriceChartRenderer;

import com.webforj.component.Composite;
import com.webforj.component.table.Table;
import com.webforj.data.repository.CollectionRepository;

import java.util.ArrayList;
import java.util.List;

public class CryptocurrencyTable extends Composite<Table> {
  
  @SuppressWarnings("unchecked")
  private Table<Cryptocurrency> table = getBoundComponent();
  
  public CryptocurrencyTable() {
    initializeTable();
  }
  
  private void initializeTable() {
    // Add columns for cryptocurrency data
    table.addColumn("Symbol", Cryptocurrency::getSymbol).setHidden(true);
    table.addColumn("Name", Cryptocurrency::getName).setHidden(true);
    table.addColumn("Icon", Cryptocurrency::getSymbol)
        .setRenderer(new CryptoIconRenderer()).setMinWidth(250.0f);
    table.addColumn("Price", c -> formatPrice(c.getCurrentPrice()))
        .setSortable(true);
    table.addColumn("24h Change", Cryptocurrency::getPriceChange24h)
        .setRenderer(new PriceChangeRenderer())
        .setSortable(true)
        .setMinWidth(180.0f);
    table.addColumn("PriceChange24h", Cryptocurrency::getPriceChange24h).setHidden(true);
    table.addColumn("PriceChangePercentage24h", Cryptocurrency::getPriceChangePercentage24h).setHidden(true);
    table.addColumn("Market Cap", c -> formatLargeNumber(c.getMarketCap()))
        .setSortable(true);
    table.addColumn("Volume (24h)", c -> formatLargeNumber(c.getVolume24h()))
        .setSortable(true);
    table.addColumn("Price Chart", Cryptocurrency::getCurrentPrice)
        .setRenderer(new PriceChartRenderer());
    table.addColumn("PriceHistory", Cryptocurrency::getPriceHistoryJson)
        .setHidden(true);
    
    // Configure table properties
    table.setMultiSorting(true);
    table.setRowHeight(65);
    
    // Set cell part provider
    table.setCellPartProvider((crypto, column) -> {
      List<String> parts = new ArrayList<>();
      
      switch (column.getId()) {
        case "Icon" -> parts.add("icon-cell");
        case "Price" -> parts.add("price-cell");
        case "Symbol" -> parts.add("symbol-cell");
        case "24h Change" -> parts.add("change-cell");
        default -> { /* No special part for other columns */ }
      }
      
      return parts;
    });
  }
  
  public void setData(List<Cryptocurrency> cryptocurrencies) {
    table.setRepository(new CollectionRepository<>(cryptocurrencies));
  }
  
  public CollectionRepository<Cryptocurrency> getRepository() {
    return (CollectionRepository<Cryptocurrency>) table.getRepository();
  }
  
  private String formatPrice(double price) {
    if (price >= 1000) {
      return String.format("$%,.2f", price);
    } else if (price >= 1) {
      return String.format("$%.2f", price);
    } else {
      return String.format("$%.4f", price);
    }
  }
  
  private String formatLargeNumber(double number) {
    if (number >= 1_000_000_000_000L) {
      return String.format("$%.2fT", number / 1_000_000_000_000.0);
    } else if (number >= 1_000_000_000) {
      return String.format("$%.2fB", number / 1_000_000_000.0);
    } else if (number >= 1_000_000) {
      return String.format("$%.2fM", number / 1_000_000.0);
    } else if (number >= 1_000) {
      return String.format("$%.2fK", number / 1_000.0);
    } else {
      return String.format("$%.2f", number);
    }
  }
}