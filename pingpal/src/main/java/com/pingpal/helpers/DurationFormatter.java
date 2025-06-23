package com.pingpal.helpers;

import java.time.Duration;

public class DurationFormatter {
    
    public static String format(Duration duration) {
        long millis = duration.toMillis();
        long seconds = millis / 1000;
        long minutes = seconds / 60;
    
        String prettyTime;
    
        if (minutes > 0) {
            prettyTime = String.format("%dm %ds", minutes, seconds % 60);
        } else if (seconds > 0) {
            prettyTime = String.format("%ds", seconds);
        } else {
            prettyTime = String.format("%d ms", millis);
        }

        return prettyTime;
    }
}
