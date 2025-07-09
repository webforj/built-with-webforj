package com.webforj.builtwithwebforj.dashboard.utils.charts;

import com.webforj.builtwithwebforj.dashboard.components.analytics.ChartCard;
import com.webforj.builtwithwebforj.dashboard.components.dashboard.DashboardCard;
import com.webforj.component.googlecharts.GoogleChart;

import java.util.List;

/**
 * Interface for views that contain charts which need to be redrawn
 * when the container size changes (e.g., when drawer opens/closes).
 */
public interface ChartRedrawable {
    /**
     * Triggers a redraw of all charts in the implementing view.
     */
    void redrawCharts();
    
    /**
     * Default method to redraw DashboardCard charts.
     * Implementing classes can call this method with their DashboardCard instances.
     * 
     * @param cards DashboardCard instances to redraw
     */
    default void redrawDashboardCards(DashboardCard... cards) {
        ChartRedrawHelper.redrawDashboardCards(cards);
    }
    
    /**
     * Default method to redraw ChartCard charts.
     * Implementing classes can call this method with their ChartCard instances.
     * 
     * @param cards ChartCard instances to redraw
     */
    default void redrawChartCards(ChartCard... cards) {
        ChartRedrawHelper.redrawChartCards(cards);
    }
    
    /**
     * Default method to redraw GoogleChart instances directly.
     * Implementing classes can call this method with their GoogleChart instances.
     * 
     * @param charts GoogleChart instances to redraw
     */
    default void redrawGoogleCharts(GoogleChart... charts) {
        ChartRedrawHelper.redrawCharts(charts);
    }
    
    /**
     * Default method to redraw all charts from lists.
     * Implementing classes can call this method with lists of their chart components.
     * 
     * @param dashboardCards List of DashboardCard instances
     * @param chartCards List of ChartCard instances
     */
    default void redrawAllCharts(List<DashboardCard> dashboardCards, List<ChartCard> chartCards) {
        ChartRedrawHelper.redrawAll(dashboardCards, chartCards);
    }
}