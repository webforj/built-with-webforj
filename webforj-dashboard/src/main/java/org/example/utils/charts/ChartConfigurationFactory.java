package org.example.utils.charts;

import com.webforj.component.googlecharts.GoogleChart;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating chart configurations.
 * Provides standardized chart styling and options for different chart types
 * used in dashboard cards.
 */
public class ChartConfigurationFactory {
    
    // Color constants
    private static final String POSITIVE_COLOR = "#22c55e";
    private static final String NEGATIVE_COLOR = "#ef4444";
    private static final String GRID_COLOR = "var(--dwc-color-gray-20)";
    
    /**
     * Creates a chart configuration for dashboard cards.
     * 
     * @param chartType The type of Google Chart
     * @param percentage The percentage change (determines color)
     * @return Map of chart options
     */
    public static Map<String, Object> createDashboardCardConfiguration(GoogleChart.Type chartType, double percentage) {
        Map<String, Object> options = new HashMap<>();
        
        // Basic configuration
        String color = percentage >= 0 ? POSITIVE_COLOR : NEGATIVE_COLOR;
        options.put("colors", List.of(color));
        options.put("backgroundColor", "transparent");
        options.put("legend", "none");
        
        // Chart area configuration
        options.put("chartArea", Map.of(
            "left", 0,
            "top", 0,
            "width", "100%",
            "height", "100%"));
        
        // Axes configuration
        options.put("hAxis", createAxisConfiguration());
        options.put("vAxis", createAxisConfiguration());
        
        // Type-specific configuration
        configureChartType(options, chartType, color);
        
        // Tooltip and animation
        options.put("tooltip", Map.of("trigger", "none"));
        options.put("animation", Map.of(
            "startup", true,
            "duration", 1500,
            "easing", "out"));
        
        return options;
    }
    
    /**
     * Creates axis configuration with hidden labels and light grid lines.
     */
    private static Map<String, Object> createAxisConfiguration() {
        return Map.of(
            "textPosition", "none",
            "gridlines", Map.of("color", GRID_COLOR),
            "baselineColor", GRID_COLOR
        );
    }
    
    /**
     * Applies chart type-specific configurations.
     */
    private static void configureChartType(Map<String, Object> options, GoogleChart.Type chartType, String color) {
        switch (chartType) {
            case AREA -> {
                options.put("areaOpacity", 0.3);
                options.put("lineWidth", 2);
                options.put("pointSize", 0);
            }
            case LINE -> {
                options.put("lineWidth", 3);
                options.put("pointSize", 0);
                options.put("curveType", "function");
            }
            case COLUMN -> {
                options.put("bar", Map.of("groupWidth", "80%"));
            }
            case SCATTER -> {
                options.put("pointSize", 8);
                options.put("pointShape", "circle");
                options.put("trendlines", Map.of(
                    "0", Map.of(
                        "type", "linear",
                        "color", color,
                        "lineWidth", 2,
                        "opacity", 0.3,
                        "showR2", false,
                        "visibleInLegend", false)));
            }
            default -> {
                // Default configuration for other chart types
            }
        }
    }
    
    /**
     * Creates a minimal chart configuration for simple displays.
     */
    public static Map<String, Object> createMinimalConfiguration(String color) {
        Map<String, Object> options = new HashMap<>();
        options.put("colors", List.of(color));
        options.put("backgroundColor", "transparent");
        options.put("legend", "none");
        options.put("tooltip", Map.of("trigger", "none"));
        return options;
    }
}