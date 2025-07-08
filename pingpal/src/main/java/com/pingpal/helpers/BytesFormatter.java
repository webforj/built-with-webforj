package com.pingpal.helpers;

public class BytesFormatter {
    
    public static String format(long bytes) {
        if (bytes < 1024) return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "B";

        double result = bytes / Math.pow(1024, exp);
        return String.format("%.1f %s", result, pre);
    }

}
