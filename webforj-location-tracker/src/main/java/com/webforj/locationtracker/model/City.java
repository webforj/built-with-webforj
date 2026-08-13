package com.webforj.locationtracker.model;

/**
 * Cities the demo ships with. Each entry carries an optional
 * {@link #getPhotoUrl() photo URL} — a stable Wikimedia Commons CDN
 * thumbnail sourced from the Wikipedia REST summary API. When present the
 * FriendCard sets it as the hero background; when absent (or the request
 * fails) the CSS gradient defined for the city's {@link #getSlug() slug}
 * takes over so the card always looks intentional.
 *
 * <p>The 8 original slugs also have signature CSS gradients in
 * {@code app.css}; new slugs added here will use a neutral gradient.</p>
 */
public enum City {
  // (label, country, flag, lat, lon, slug, photoUrl)
  PARIS         ("Paris",          "France",         "🇫🇷", 48.8566,   2.3522, "paris",         "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg/1280px-La_Tour_Eiffel_vue_de_la_Tour_Saint-Jacques%2C_Paris_ao%C3%BBt_2014_%282%29.jpg"),
  TOKYO         ("Tokyo",          "Japan",          "🇯🇵", 35.6762, 139.6503, "tokyo",         "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/1280px-Skyscrapers_of_Shinjuku_2009_January.jpg"),
  CAPE_TOWN     ("Cape Town",      "South Africa",   "🇿🇦",-33.9249,  18.4241, "cape-town",     "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/Camps_bay_%2853460319478%29_%28cropped%29.jpg/1280px-Camps_bay_%2853460319478%29_%28cropped%29.jpg"),
  SYDNEY        ("Sydney",         "Australia",      "🇦🇺",-33.8688, 151.2093, "sydney",        "https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Sydney_Opera_House_and_Harbour_Bridge_Dusk_%282%29_2019-06-21.jpg/1280px-Sydney_Opera_House_and_Harbour_Bridge_Dusk_%282%29_2019-06-21.jpg"),
  NEW_YORK      ("New York",       "United States",  "🇺🇸", 40.7128, -74.0060, "new-york",      "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/View_of_Empire_State_Building_from_Rockefeller_Center_New_York_City_dllu_%28cropped%29.jpg/1280px-View_of_Empire_State_Building_from_Rockefeller_Center_New_York_City_dllu_%28cropped%29.jpg"),
  REYKJAVIK     ("Reykjavík",      "Iceland",        "🇮🇸", 64.1466, -21.9426, "reykjavik",     "https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Reykjav%C3%ADk%2C_view_from_Hallgr%C3%ADmskirkja_%282%29.jpg/1280px-Reykjav%C3%ADk%2C_view_from_Hallgr%C3%ADmskirkja_%282%29.jpg"),
  RIO           ("Rio de Janeiro", "Brazil",         "🇧🇷",-22.9068, -43.1729, "rio",           "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Cidade_Maravilhosa.jpg/1280px-Cidade_Maravilhosa.jpg"),
  MARRAKECH     ("Marrakech",      "Morocco",        "🇲🇦", 31.6295,  -7.9811, "marrakech",     "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Pavillon_Menarag%C3%A4rten.jpg/1280px-Pavillon_Menarag%C3%A4rten.jpg"),
  LONDON        ("London",         "United Kingdom", "🇬🇧", 51.5074,  -0.1278, "london",        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/London_Skyline_%28125508655%29.jpeg/1280px-London_Skyline_%28125508655%29.jpeg"),
  ROME          ("Rome",           "Italy",          "🇮🇹", 41.9028,  12.4964, "rome",          "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Trevi_Fountain%2C_Rome%2C_Italy_2_-_May_2007.jpg/1280px-Trevi_Fountain%2C_Rome%2C_Italy_2_-_May_2007.jpg"),
  BARCELONA     ("Barcelona",      "Spain",          "🇪🇸", 41.3851,   2.1734, "barcelona",     "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a6/Evening_light_over_Barcelona.jpg/1280px-Evening_light_over_Barcelona.jpg"),
  AMSTERDAM     ("Amsterdam",      "Netherlands",    "🇳🇱", 52.3676,   4.9041, "amsterdam",     "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Imagen_de_los_canales_conc%C3%A9ntricos_en_%C3%81msterdam.png/1280px-Imagen_de_los_canales_conc%C3%A9ntricos_en_%C3%81msterdam.png"),
  BERLIN        ("Berlin",         "Germany",        "🇩🇪", 52.5200,  13.4050, "berlin",        "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Museumsinsel_Berlin_Juli_2021_1_%28cropped%29_b.jpg/1280px-Museumsinsel_Berlin_Juli_2021_1_%28cropped%29_b.jpg"),
  PRAGUE        ("Prague",         "Czechia",        "🇨🇿", 50.0755,  14.4378, "prague",        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Prague_%286365119737%29.jpg/1280px-Prague_%286365119737%29.jpg"),
  ISTANBUL      ("Istanbul",       "Türkiye",        "🇹🇷", 41.0082,  28.9784, "istanbul",      "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Historical_peninsula_and_modern_skyline_of_Istanbul.jpg/1280px-Historical_peninsula_and_modern_skyline_of_Istanbul.jpg"),
  DUBAI         ("Dubai",          "UAE",            "🇦🇪", 25.2048,  55.2708, "dubai",         "https://upload.wikimedia.org/wikipedia/en/thumb/c/c7/Burj_Khalifa_2021.jpg/1280px-Burj_Khalifa_2021.jpg"),
  SINGAPORE     ("Singapore",      "Singapore",      "🇸🇬",  1.3521, 103.8198, "singapore",     "https://upload.wikimedia.org/wikipedia/commons/thumb/1/16/Marina_Bay_Singapore-3499.jpg/1280px-Marina_Bay_Singapore-3499.jpg"),
  HONG_KONG     ("Hong Kong",      "Hong Kong SAR",  "🇭🇰", 22.3193, 114.1694, "hong-kong",     "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a4/Hong_Kong_Harbour_Night_2019-06-11.jpg/1280px-Hong_Kong_Harbour_Night_2019-06-11.jpg"),
  SEOUL         ("Seoul",          "South Korea",    "🇰🇷", 37.5665, 126.9780, "seoul",         "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/%EC%A4%91%ED%99%94%EC%A0%84%EC%9D%98_%EB%82%AE.jpg/1280px-%EC%A4%91%ED%99%94%EC%A0%84%EC%9D%98_%EB%82%AE.jpg"),
  BANGKOK       ("Bangkok",        "Thailand",       "🇹🇭", 13.7563, 100.5018, "bangkok",       "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/4Y1A1159_Bangkok_%2833536795515%29.jpg/1280px-4Y1A1159_Bangkok_%2833536795515%29.jpg"),
  MUMBAI        ("Mumbai",         "India",          "🇮🇳", 19.0760,  72.8777, "mumbai",        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3a/Mumbai_03-2016_30_Gateway_of_India.jpg/1280px-Mumbai_03-2016_30_Gateway_of_India.jpg"),
  NAIROBI       ("Nairobi",        "Kenya",          "🇰🇪", -1.2921,  36.8219, "nairobi",       "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Nairobi_skyline_from_Gem_Hotel.jpg/1280px-Nairobi_skyline_from_Gem_Hotel.jpg"),
  CAIRO         ("Cairo",          "Egypt",          "🇪🇬", 30.0444,  31.2357, "cairo",         "https://upload.wikimedia.org/wikipedia/commons/thumb/7/72/Cairo_Opera_House%2C_Al_Hurriyah_Park_and_the_Nile_river_%2814797782354%29.jpg/1280px-Cairo_Opera_House%2C_Al_Hurriyah_Park_and_the_Nile_river_%2814797782354%29.jpg"),
  SAN_FRANCISCO ("San Francisco",  "United States",  "🇺🇸", 37.7749,-122.4194, "san-francisco", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Golden_Gate_Bridge_as_seen_from_Battery_East.jpg/1280px-Golden_Gate_Bridge_as_seen_from_Battery_East.jpg"),
  BUENOS_AIRES  ("Buenos Aires",   "Argentina",      "🇦🇷",-34.6037, -58.3816, "buenos-aires",  "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Puerto_Madero%2C_Buenos_Aires_%2840689219792%29_%28cropped%29.jpg/1280px-Puerto_Madero%2C_Buenos_Aires_%2840689219792%29_%28cropped%29.jpg");

  private final String label;
  private final String country;
  private final String flag;
  private final double latitude;
  private final double longitude;
  private final String slug;
  private final String photoUrl;

  City(String label, String country, String flag,
       double latitude, double longitude, String slug, String photoUrl) {
    this.label = label;
    this.country = country;
    this.flag = flag;
    this.latitude = latitude;
    this.longitude = longitude;
    this.slug = slug;
    this.photoUrl = photoUrl;
  }

  public String getLabel() { return label; }
  public String getCountry() { return country; }
  public String getFlag() { return flag; }
  public double getLatitude() { return latitude; }
  public double getLongitude() { return longitude; }
  public String getSlug() { return slug; }
  public String getPhotoUrl() { return photoUrl; }

  @Override
  public String toString() { return label; }
}
