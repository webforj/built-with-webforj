package com.webforjgeolocation.components;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EmblemLoader {

  private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

  private EmblemLoader() {
  }

  public static String svgFor(String wonderId) {
    return CACHE.computeIfAbsent(wonderId, EmblemLoader::loadFromClasspath);
  }

  private static String loadFromClasspath(String wonderId) {
    String path = "/static/svg/" + wonderId + ".svg";
    try (InputStream is = EmblemLoader.class.getResourceAsStream(path)) {
      if (is == null) {
        return "";
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      return "";
    }
  }
}
