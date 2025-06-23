package com.pingpal.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Env {
    private static final Map<String, String> envVars = new HashMap<>();

    static {
        loadEnvFile(".env");
    }

    private static void loadEnvFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) return;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    int equalsIndex = line.indexOf('=');
                    if (equalsIndex == -1) continue;

                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim().replaceAll("^\"|\"$", "");

                    envVars.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load .env file", e);
        }
    }

    public static String get(String key) {
        String systemValue = System.getenv(key);
        return envVars.containsKey(key) ? envVars.get(key) : systemValue;
    }
}