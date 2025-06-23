package com.pingpal.helpers;

import java.util.Map;

public class StatusCodeFormatter {
    
    private static final Map<Integer, String> statusMessages = Map.ofEntries(
        Map.entry(100, "Continue"),
        Map.entry(200, "OK"),
        Map.entry(201, "Created"),
        Map.entry(202, "Accepted"),
        Map.entry(204, "No Content"),
        Map.entry(301, "Moved Permanently"),
        Map.entry(302, "Found"),
        Map.entry(400, "Bad Request"),
        Map.entry(401, "Unauthorized"),
        Map.entry(403, "Forbidden"),
        Map.entry(404, "Not Found"),
        Map.entry(500, "Internal Server Error"),
        Map.entry(502, "Bad Gateway"),
        Map.entry(503, "Service Unavailable")
    );

    public static String format(int code) {
        return String.valueOf(code) + " " + statusMessages.getOrDefault(code, "Unknown");
    }

}
