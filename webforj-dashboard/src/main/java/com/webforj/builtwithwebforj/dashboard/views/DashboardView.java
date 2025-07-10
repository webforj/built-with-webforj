package com.webforj.builtwithwebforj.dashboard.views;

import com.webforj.Interval;
import com.webforj.builtwithwebforj.dashboard.components.dashboard.CryptocurrencyTable;
import com.webforj.builtwithwebforj.dashboard.components.dashboard.DashboardCard;
import com.webforj.builtwithwebforj.dashboard.components.dashboard.DashboardToolbar;
import com.webforj.builtwithwebforj.dashboard.models.Cryptocurrency;
import com.webforj.builtwithwebforj.dashboard.services.CryptocurrencyService;
import com.webforj.builtwithwebforj.dashboard.utils.charts.ChartRedrawable;
import com.webforj.builtwithwebforj.dashboard.utils.charts.DashboardChartBuilder;
import com.webforj.component.Composite;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;

@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Cryptocurrency Dashboard")
public class DashboardView extends Composite<FlexLayout> implements ChartRedrawable {
  private final FlexLayout self = getBoundComponent();
  private final CryptocurrencyService cryptoService = new CryptocurrencyService();
  private final DashboardChartBuilder chartBuilder = new DashboardChartBuilder();
  private CryptocurrencyTable cryptoTable;
  private DashboardToolbar toolbar;
  private List<Cryptocurrency> cryptocurrencies;
  private Interval interval;
  private DashboardCard card1;
  private DashboardCard card2;
  private DashboardCard card3;

  public DashboardView() {
    self.addClassName("dashboard-view");
    self.setDirection(FlexDirection.COLUMN);

    // Create toolbar
    toolbar = new DashboardToolbar();

    // Create cryptocurrency table
    cryptoTable = new CryptocurrencyTable();

    // Generate and set data
    cryptocurrencies = cryptoService.generateCryptocurrencies();
    cryptoTable.setData(cryptocurrencies);

    // Create dashboard cards using the new architecture
    card1 = createCard("Global Market Cap", 2875000000000.0, 3.45, GoogleChart.Type.AREA);
    card2 = createCard("24 Hour Volume", 98500000000.0, -5.23, GoogleChart.Type.SCATTER);
    card3 = createCard("Bitcoin Dominance", 52.7, 1.28, GoogleChart.Type.COLUMN);

    // Create cards layout
    FlexLayout cards = new FlexLayout(card1, card2, card3);
    cards.addClassName("dashboard-view__cards");

    // Add wrapper for cards to control order on mobile
    FlexLayout cardsWrapper = new FlexLayout(cards);
    cardsWrapper.addClassName("dashboard-view__cards-wrapper");

    // Add wrapper for table to control order on mobile
    FlexLayout tableWrapper = new FlexLayout(cryptoTable);
    tableWrapper.addClassName("dashboard-view__table-wrapper");

    // Add components to view
    self.add(cardsWrapper, tableWrapper);

    // Update prices every 2 seconds
    interval = new Interval(1f, e -> {
      cryptoService.updatePrices(cryptocurrencies);
      cryptoTable.getRepository().commit();
    });

    interval.start();
  }

  /**
   * Creates a dashboard card with chart using the new architecture.
   *
   * @param title      The card title
   * @param value      The card value
   * @param percentage The percentage change
   * @param chartType  The type of chart to create
   * @return A configured DashboardCard
   */
  private DashboardCard createCard(String title, double value, double percentage, GoogleChart.Type chartType) {
    // Build the chart using the chart builder
    GoogleChart chart = chartBuilder.buildDashboardChart(chartType, percentage);

    // Create the card with data and chart
    return new DashboardCard(title, value, percentage, chart);
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
