package org.example.views;

import org.example.components.DashboardCard;
import org.example.models.Cryptocurrency;
import org.example.services.CryptocurrencyService;
import org.example.utils.CryptoIconRenderer;
import org.example.utils.PriceChangeRenderer;
import org.example.utils.PriceChartRenderer;

import com.webforj.Interval;
import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Table;
import com.webforj.data.repository.CollectionRepository;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Cryptocurrency Dashboard")
public class DashboardView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private CryptocurrencyService cryptoService = new CryptocurrencyService();
  Interval interval;

  public DashboardView() {
    self.addClassName("dashboard-view");
    self.setStyle("overflow-y", "auto");

    Table<Cryptocurrency> table = new Table<>();

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
    table.setMultiSorting(true);
    table.setRowHeight(65);

    List<Cryptocurrency> cryptocurrencies = cryptoService.generateCryptocurrencies();
    table.setRepository(new CollectionRepository<>(cryptocurrencies));

    table.setCellPartProvider((crypto, column) -> {
      List<String> parts = new ArrayList<>();

      switch (column.getId()) {
        case "Icon" -> parts.add("icon-cell");
        case "Price" -> parts.add("price-cell");
        case "Symbol" -> parts.add("symbol-cell");
        case "24h Change" -> parts.add("change-cell");
      }

      return parts;
    });

    DashboardCard card = new DashboardCard("Global Market Cap", 2875000000000.0, 3.45, "$2.88 Trillion total cryptocurrency market capitalization");
    DashboardCard card2 = new DashboardCard("24 Hour Volume", 98500000000.0, -5.23, "$98.5 Billion traded across all exchanges");
    DashboardCard card3 = new DashboardCard("Bitcoin Dominance", 52.7, 1.28, "BTC market share of total crypto market cap");
    FlexLayout cards = new FlexLayout(card, card2, card3);
    cards.setJustifyContent(FlexJustifyContent.BETWEEN)
         .setWidth("100%")
         .setWrap(FlexWrap.WRAP)
         .setStyle("gap", "var(--dwc-space-m)");
    self.setDirection(FlexDirection.COLUMN);
    self.add(cards, table);

    // Update prices every 2 seconds
    interval = new Interval(2f, e -> {
      cryptoService.updatePrices(cryptocurrencies);
      table.getRepository().commit();
    });

    interval.start();
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

  // private String formatPercentage(double percentage) {
  //   String sign = percentage >= 0 ? "+" : "";
  //   return String.format("%s%.2f%%", sign, percentage);
  // }

  // private String formatPriceChange(double change) {
  //   String sign = change >= 0 ? "+" : "";
  //   if (Math.abs(change) >= 1) {
  //     return String.format("%s$%.2f", sign, change);
  //   } else {
  //     return String.format("%s$%.4f", sign, change);
  //   }
  // }

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

  @Override
  protected void onDidDestroy() {
    super.onDidDestroy();
    interval.stop();
  }
}