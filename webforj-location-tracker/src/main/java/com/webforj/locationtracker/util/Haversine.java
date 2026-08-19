package com.webforj.locationtracker.util;

import java.util.Locale;

public final class Haversine {
  private static final double EARTH_RADIUS_KM = 6371.0;

  private Haversine() {}

  public static double kilometersBetween(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS_KM * c;
  }

  // The labels are English, so the numbers are formatted with a fixed locale rather
  // than whatever locale the server happens to boot with.
  public static String formatKm(double km) {
    if (km < 1) return "< 1 km";
    if (km < 10) return String.format(Locale.US, "%.1f km", km);
    return String.format(Locale.US, "%,d km", Math.round(km));
  }

  public static String formatMiles(double km) {
    double mi = km * 0.621371;
    if (mi < 1) return "< 1 mi";
    if (mi < 10) return String.format(Locale.US, "%.1f mi", mi);
    return String.format(Locale.US, "%,d mi", Math.round(mi));
  }

  public static String formatDistance(double km, boolean useMiles) {
    return useMiles ? formatMiles(km) : formatKm(km);
  }
}
