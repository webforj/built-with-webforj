package org.example.views;

import org.example.components.CryptocurrencyTable;
import org.example.components.DashboardCard;
import org.example.models.Cryptocurrency;
import org.example.services.CryptocurrencyService;

import com.webforj.Interval;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;

@Route(value = "/", outlet = MainLayout.class)
@StyleSheet("ws://dashboard-view.css")
@FrameTitle("Cryptocurrency Dashboard")
public class DashboardView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private CryptocurrencyService cryptoService = new CryptocurrencyService();
  private CryptocurrencyTable cryptoTable;
  private List<Cryptocurrency> cryptocurrencies;
  private Interval interval;

  public DashboardView() {
    self.addClassName("dashboard-view");
    self.setDirection(FlexDirection.COLUMN);

    // Create cryptocurrency table
    cryptoTable = new CryptocurrencyTable();
    
    // Generate and set data
    cryptocurrencies = cryptoService.generateCryptocurrencies();
    cryptoTable.setData(cryptocurrencies);

    // Create dashboard cards
    DashboardCard card = new DashboardCard("Global Market Cap", 2875000000000.0, 3.45);
    DashboardCard card2 = new DashboardCard("24 Hour Volume", 98500000000.0, -5.23, GoogleChart.Type.SCATTER);
    DashboardCard card3 = new DashboardCard("Bitcoin Dominance", 52.7, 1.28, GoogleChart.Type.COLUMN);
    
    // Create cards layout
    FlexLayout cards = new FlexLayout(card, card2, card3);
    cards.addClassName("dashboard-view__cards");
    
    // Add components to view
    self.add(cards, cryptoTable);

    // Update prices every 2 seconds
    interval = new Interval(1f, e -> {
      cryptoService.updatePrices(cryptocurrencies);
      cryptoTable.getRepository().commit();
    });

    interval.start();
  }

  @Override
  protected void onDidDestroy() {
    super.onDidDestroy();
    interval.stop();
  }
}