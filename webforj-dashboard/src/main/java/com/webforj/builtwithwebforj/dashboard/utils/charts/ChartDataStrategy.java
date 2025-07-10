package com.webforj.builtwithwebforj.dashboard.utils.charts;

import com.webforj.component.googlecharts.GoogleChart;
import java.util.List;

/**
 * Strategy interface for generating chart data.
 * This allows different implementations for various types of data generation
 * while keeping the chart configuration and UI components decoupled.
 */
public interface ChartDataStrategy {
    
    /**
     * Generates chart data based on the provided parameters.
     * 
     * @param percentage The percentage change that affects the trend
     * @param chartType The type of Google Chart being generated
     * @return List of chart data in Google Charts format
     */
    List<Object> generateData(double percentage, GoogleChart.Type chartType);
}