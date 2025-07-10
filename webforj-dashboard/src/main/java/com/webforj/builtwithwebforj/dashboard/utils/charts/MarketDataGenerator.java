package com.webforj.builtwithwebforj.dashboard.utils.charts;

import com.webforj.component.googlecharts.GoogleChart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Generates realistic market data for dashboard charts.
 * Implements ChartDataStrategy to provide market-like data patterns
 * with trends, volatility, and occasional spikes.
 */
public class MarketDataGenerator implements ChartDataStrategy {
    
    private final Random random;
    
    public MarketDataGenerator() {
        this.random = new Random();
    }
    
    /**
     * Constructor with seed for reproducible data (useful for testing)
     */
    public MarketDataGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    @Override
    public List<Object> generateData(double percentage, GoogleChart.Type chartType) {
        List<Object> data = new ArrayList<>();
        
        // Header row
        data.add(Arrays.asList("Time", "Value"));
        
        // Configuration based on chart type
        int dataPoints = getDataPointCount(chartType);
        double baseValue = 100;
        double trendFactor = percentage >= 0 ? 1.005 : 0.995;
        double momentum = 0;
        
        for (int i = 0; i < dataPoints; i++) {
            String label = generateLabel(i, chartType);
            double value = generateDataPoint(baseValue, momentum, trendFactor, chartType);
            
            data.add(Arrays.asList(label, Math.max(value, 10)));
            
            // Update momentum for next iteration
            momentum = updateMomentum(momentum);
            
            // Apply trend with randomness
            baseValue *= trendFactor * (0.98 + random.nextDouble() * 0.04);
        }
        
        return data;
    }
    
    private int getDataPointCount(GoogleChart.Type chartType) {
        return switch (chartType) {
            case COLUMN -> 10;
            case SCATTER -> 30;
            default -> 20;
        };
    }
    
    private String generateLabel(int index, GoogleChart.Type chartType) {
        if (chartType == GoogleChart.Type.COLUMN) {
            // Use time labels for column charts (e.g., "8:00", "10:00", etc.)
            return String.format("%d:00", (index * 2 + 8) % 24);
        } else {
            return String.valueOf(index);
        }
    }
    
    private double generateDataPoint(double baseValue, double momentum, double trendFactor, GoogleChart.Type chartType) {
        double value = baseValue + momentum;
        
        // For scatter charts, add more variation to create a cloud effect
        if (chartType == GoogleChart.Type.SCATTER) {
            value += (random.nextDouble() - 0.5) * 10;
        }
        
        return value;
    }
    
    private double updateMomentum(double currentMomentum) {
        // Generate realistic market movement
        double randomChange = (random.nextDouble() - 0.5) * 15;
        double newMomentum = currentMomentum * 0.7 + randomChange * 0.3;
        
        // Add occasional spikes (10% chance)
        if (random.nextDouble() < 0.1) {
            newMomentum += (random.nextDouble() - 0.5) * 20;
        }
        
        return newMomentum;
    }
}