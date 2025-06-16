package org.example.components.dashboard;

import org.example.models.Cryptocurrency;
import org.example.utils.CryptoIconRenderer;
import org.example.utils.FormatUtils;
import org.example.utils.PriceChangeRenderer;
import org.example.utils.PriceChartRenderer;

import com.webforj.component.Composite;
import com.webforj.component.table.Table;
import com.webforj.data.repository.CollectionRepository;

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
    table.addColumn("Price", c -> FormatUtils.formatPrice(c.getCurrentPrice()))
        .setSortable(true);
    table.addColumn("24h Change", Cryptocurrency::getPriceChange24h)
        .setRenderer(new PriceChangeRenderer())
        .setSortable(true)
        .setMinWidth(180.0f);
    table.addColumn("PriceChange24h", Cryptocurrency::getPriceChange24h).setHidden(true);
    table.addColumn("PriceChangePercentage24h", Cryptocurrency::getPriceChangePercentage24h).setHidden(true);
    table.addColumn("Market Cap", c -> FormatUtils.formatLargeNumber(c.getMarketCap()))
        .setSortable(true);
    table.addColumn("Volume (24h)", c -> FormatUtils.formatLargeNumber(c.getVolume24h()))
        .setSortable(true);
    table.addColumn("Price Chart", Cryptocurrency::getCurrentPrice)
        .setRenderer(new PriceChartRenderer());
    table.addColumn("PriceHistory", Cryptocurrency::getPriceHistoryJson)
        .setHidden(true);
    
    // Configure table properties
    table.setMultiSorting(true);
    table.setRowHeight(65);
  }
  
  public void setData(List<Cryptocurrency> cryptocurrencies) {
    table.setRepository(new CollectionRepository<>(cryptocurrencies));
  }
  
  public CollectionRepository<Cryptocurrency> getRepository() {
    return (CollectionRepository<Cryptocurrency>) table.getRepository();
  }
}