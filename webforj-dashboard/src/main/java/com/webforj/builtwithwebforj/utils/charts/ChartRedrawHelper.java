package com.webforj.builtwithwebforj.utils.charts;

import com.webforj.builtwithwebforj.components.analytics.ChartCard;
import com.webforj.builtwithwebforj.components.dashboard.DashboardCard;
import com.webforj.component.googlecharts.GoogleChart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Utility class for handling chart redraw operations with null safety
 */
public class ChartRedrawHelper {
  
  /**
   * Redraws all provided charts, handling null values gracefully
   * @param charts varargs of GoogleChart instances (nulls are safely ignored)
   */
  public static void redrawCharts(GoogleChart... charts) {
    if (charts == null) return;
    
    Arrays.stream(charts)
        .filter(Objects::nonNull)
        .forEach(GoogleChart::redraw);
  }
  
  /**
   * Redraws charts from DashboardCard instances
   * @param cards varargs of DashboardCard instances (nulls are safely ignored)
   */
  public static void redrawDashboardCards(DashboardCard... cards) {
    if (cards == null) return;
    
    Arrays.stream(cards)
        .filter(Objects::nonNull)
        .map(DashboardCard::getChart)
        .filter(Objects::nonNull)
        .forEach(GoogleChart::redraw);
  }
  
  /**
   * Redraws charts from ChartCard instances
   * @param cards varargs of ChartCard instances (nulls are safely ignored)
   */
  public static void redrawChartCards(ChartCard... cards) {
    if (cards == null) return;
    
    Arrays.stream(cards)
        .filter(Objects::nonNull)
        .map(ChartCard::getChart)
        .filter(Objects::nonNull)
        .forEach(GoogleChart::redraw);
  }
  
  /**
   * Redraws all charts from mixed sources
   * @param dashboardCards collection of DashboardCard instances
   * @param chartCards collection of ChartCard instances
   */
  public static void redrawAll(Collection<DashboardCard> dashboardCards, Collection<ChartCard> chartCards) {
    Stream.concat(
        dashboardCards.stream()
            .filter(Objects::nonNull)
            .map(DashboardCard::getChart),
        chartCards.stream()
            .filter(Objects::nonNull)
            .map(ChartCard::getChart)
    )
    .filter(Objects::nonNull)
    .forEach(GoogleChart::redraw);
  }
}