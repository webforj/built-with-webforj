package com.webforjgeolocation.geo;

import com.webforjgeolocation.model.Coordinates;
import java.text.NumberFormat;
import java.util.Locale;

public final class GeoMath {
  private static final double EARTH_RADIUS_KM = 6371.0088;

  private static final String[] COMPASS_WORDS = {
      "north", "northeast", "east", "southeast",
      "south", "southwest", "west", "northwest"
  };

  private GeoMath() {
  }

  public static double haversineKm(Coordinates a, Coordinates b) {
    double dLat = Math.toRadians(b.latitude() - a.latitude());
    double dLng = Math.toRadians(b.longitude() - a.longitude());
    double la1 = Math.toRadians(a.latitude());
    double la2 = Math.toRadians(b.latitude());
    double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(la1) * Math.cos(la2) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    return 2 * EARTH_RADIUS_KM * Math.asin(Math.min(1.0, Math.sqrt(h)));
  }

  public static double initialBearingDegrees(Coordinates from, Coordinates to) {
    double la1 = Math.toRadians(from.latitude());
    double la2 = Math.toRadians(to.latitude());
    double dLng = Math.toRadians(to.longitude() - from.longitude());
    double y = Math.sin(dLng) * Math.cos(la2);
    double x = Math.cos(la1) * Math.sin(la2)
        - Math.sin(la1) * Math.cos(la2) * Math.cos(dLng);
    double deg = Math.toDegrees(Math.atan2(y, x));
    return (deg + 360.0) % 360.0;
  }

  public static String bearingWords(double degrees) {
    int idx = (int) Math.round(degrees / 45.0) % 8;
    if (idx < 0) {
      idx += 8;
    }
    return COMPASS_WORDS[idx];
  }

  public static String formatDistance(double km, Unit unit) {
    double v = km * unit.factor();
    if (v >= 100.0) {
      return NumberFormat.getNumberInstance(Locale.US).format(Math.round(v));
    } else if (v >= 10.0) {
      return String.format(Locale.US, "%.0f", v);
    } else {
      return String.format(Locale.US, "%.1f", v);
    }
  }
}
