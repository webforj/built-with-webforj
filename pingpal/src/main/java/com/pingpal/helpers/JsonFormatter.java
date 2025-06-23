package com.pingpal.helpers;

import java.io.StringWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;

public class JsonFormatter {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String format(JsonElement jsonElement) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setIndent("    "); // 4 spaces

        gson.toJson(jsonElement, jsonWriter);

        return stringWriter.toString();
    }
    
}
