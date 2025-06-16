package org.example.views;

import org.example.ChartRedrawable;
import org.example.components.dashboard.CryptocurrencyTable;
import org.example.components.dashboard.DashboardCard;
import org.example.models.Cryptocurrency;
import org.example.services.CryptocurrencyService;
import org.example.utils.ChartRedrawHelper;

import com.webforj.Interval;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;

@Route(value = "/", outlet = MainLayout.class)
@StyleSheet("ws://dashboard-view.css")
@FrameTitle("Cryptocurrency Dashboard")
public class DashboardView extends Composite<FlexLayout> implements ChartRedrawable {
  private final FlexLayout self = getBoundComponent();
  private final CryptocurrencyService cryptoService = new CryptocurrencyService();
  private CryptocurrencyTable cryptoTable;
  private List<Cryptocurrency> cryptocurrencies;
  private Interval interval;
  private DashboardCard card1;
  private DashboardCard card2;
  private DashboardCard card3;

  public DashboardView() {
    self.addClassName("dashboard-view");
    self.setDirection(FlexDirection.COLUMN);

    // Create cryptocurrency table
    cryptoTable = new CryptocurrencyTable();
    
    // Generate and set data
    cryptocurrencies = cryptoService.generateCryptocurrencies();
    cryptoTable.setData(cryptocurrencies);

    // Create dashboard cards
    card1 = new DashboardCard("Global Market Cap", 2875000000000.0, 3.45);
    card2 = new DashboardCard("24 Hour Volume", 98500000000.0, -5.23, GoogleChart.Type.SCATTER);
    card3 = new DashboardCard("Bitcoin Dominance", 52.7, 1.28, GoogleChart.Type.COLUMN);
    
    // Create cards layout
    FlexLayout cards = new FlexLayout(card1, card2, card3);
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
  
  @Override
  public void redrawCharts() {
    // Using the default interface method
    redrawDashboardCards(card1, card2, card3);
  }
}