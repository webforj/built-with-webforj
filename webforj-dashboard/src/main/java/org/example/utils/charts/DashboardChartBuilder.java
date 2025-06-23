package org.example.utils.charts;

import com.webforj.component.googlecharts.GoogleChart;
import java.util.List;
import java.util.Map;

/**
 * Builder class for creating configured Google Charts for dashboard cards.
 * This class encapsulates the chart creation process and provides a clean API
 * for building charts with data and configuration.
 */
public class DashboardChartBuilder {
    
    private final ChartDataStrategy dataStrategy;
    
    /**
     * Creates a new DashboardChartBuilder with the default market data generator.
     */
    public DashboardChartBuilder() {
        this.dataStrategy = new MarketDataGenerator();
    }
    
    /**
     * Creates a new DashboardChartBuilder with a custom data strategy.
     * 
     * @param dataStrategy The strategy to use for generating chart data
     */
    public DashboardChartBuilder(ChartDataStrategy dataStrategy) {
        this.dataStrategy = dataStrategy;
    }
    
    /**
     * Builds a complete GoogleChart for dashboard cards.
     * 
     * @param chartType The type of chart to create
     * @param percentage The percentage change that affects styling and data trend
     * @return A fully configured GoogleChart ready for use
     */
    public GoogleChart buildDashboardChart(GoogleChart.Type chartType, double percentage) {
        // Create the chart
        GoogleChart chart = new GoogleChart(chartType);
        
        // Set dimensions
        chart.setHeight("100px");
        chart.setWidth("100%");
        
        // Generate and set data
        List<Object> data = dataStrategy.generateData(percentage, chartType);
        chart.setData(data);
        
        // Configure options
        Map<String, Object> options = ChartConfigurationFactory.createDashboardCardConfiguration(chartType, percentage);
        chart.setOptions(options);
        
        return chart;
    }
    
    /**
     * Builds a chart with custom data.
     * Useful when you have specific data to display rather than generated data.
     * 
     * @param chartType The type of chart to create
     * @param percentage The percentage change for styling
     * @param data Custom data to use instead of generated data
     * @return A configured GoogleChart with the provided data
     */
    public GoogleChart buildDashboardChartWithData(GoogleChart.Type chartType, double percentage, List<Object> data) {
        GoogleChart chart = new GoogleChart(chartType);
        
        chart.setHeight("100px");
        chart.setWidth("100%");
        chart.setData(data);
        
        Map<String, Object> options = ChartConfigurationFactory.createDashboardCardConfiguration(chartType, percentage);
        chart.setOptions(options);
        
        return chart;
    }
    
    /**
     * Creates a chart configuration without the chart component.
     * Useful when you need to configure an existing chart.
     * 
     * @param chartType The type of chart
     * @param percentage The percentage change
     * @return Chart configuration map
     */
    public Map<String, Object> buildConfiguration(GoogleChart.Type chartType, double percentage) {
        return ChartConfigurationFactory.createDashboardCardConfiguration(chartType, percentage);
    }
    
    /**
     * Generates data without creating a chart.
     * Useful for testing or when you need raw data.
     * 
     * @param chartType The type of chart the data is for
     * @param percentage The percentage change affecting the trend
     * @return Generated chart data
     */
    public List<Object> generateData(GoogleChart.Type chartType, double percentage) {
        return dataStrategy.generateData(percentage, chartType);
    }
}