package com.webforj.property.data;

import com.webforj.property.model.Property;
import com.webforj.property.model.PropertyStatus;
import com.webforj.property.model.PropertyType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sample property data provider for the PropertyView demo.
 *
 * <p>Provides 50 realistic sample properties distributed across the Austin, TX metropolitan area
 * with varied types, prices, and features.
 */
public final class PropertyDataProvider {

  private static final List<Property> PROPERTIES = createSampleProperties();

  private PropertyDataProvider() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Gets all sample properties.
   *
   * @return unmodifiable list of properties
   */
  public static List<Property> getProperties() {
    return Collections.unmodifiableList(PROPERTIES);
  }

  /**
   * Gets a property by ID.
   *
   * @param id the property ID
   * @return the property, or null if not found
   */
  public static Property getPropertyById(String id) {
    return PROPERTIES.stream().filter(p -> p.id().equals(id)).findFirst().orElse(null);
  }

  private static List<Property> createSampleProperties() {
    List<Property> properties = new ArrayList<>();

    // ========== DOWNTOWN AUSTIN ==========
    properties.add(
        new Property(
            "prop-001",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            525000,
            "360 Nueces St #2801",
            "Austin",
            "TX",
            "78701",
            -97.7494,
            30.2669,
            2,
            2,
            1250,
            null,
            2019,
            "Stunning downtown condo with panoramic city views from the 28th floor. "
                + "Modern finishes, floor-to-ceiling windows, and resort-style amenities.",
            "https://picsum.photos/seed/prop001/300/200"));

    properties.add(
        new Property(
            "prop-002",
            PropertyType.APARTMENT,
            PropertyStatus.FOR_SALE,
            385000,
            "501 West Ave #1205",
            "Austin",
            "TX",
            "78701",
            -97.7521,
            30.2711,
            1,
            1,
            850,
            null,
            2015,
            "Urban living at its finest. Walk to restaurants, entertainment, and Lady Bird Lake. "
                + "Building features rooftop pool and fitness center.",
            "https://picsum.photos/seed/prop002/300/200"));

    properties.add(
        new Property(
            "prop-019",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            875000,
            "200 Congress Ave #25A",
            "Austin",
            "TX",
            "78701",
            -97.7431,
            30.2650,
            3,
            2,
            1850,
            null,
            2021,
            "Premier downtown high-rise living with stunning Capitol views. Chef's kitchen, "
                + "spa-like bathrooms, and 24-hour concierge service.",
            "https://picsum.photos/seed/prop019/300/200"));

    properties.add(
        new Property(
            "prop-020",
            PropertyType.APARTMENT,
            PropertyStatus.PENDING,
            465000,
            "555 E 5th St #805",
            "Austin",
            "TX",
            "78701",
            -97.7389,
            30.2678,
            2,
            2,
            1100,
            null,
            2018,
            "Modern Rainey Street district apartment. Walkable to bars, restaurants, and trails. "
                + "Floor-to-ceiling windows with city skyline views.",
            "https://picsum.photos/seed/prop020/300/200"));

    properties.add(
        new Property(
            "prop-021",
            PropertyType.COMMERCIAL,
            PropertyStatus.FOR_SALE,
            2850000,
            "614 E 6th St",
            "Austin",
            "TX",
            "78701",
            -97.7365,
            30.2675,
            null,
            null,
            5200,
            3500,
            1925,
            "Historic 6th Street commercial property. Ideal for bar, restaurant, or entertainment "
                + "venue. High foot traffic and excellent visibility.",
            "https://picsum.photos/seed/prop021/300/200"));

    // ========== EAST AUSTIN / MUELLER ==========
    properties.add(
        new Property(
            "prop-003",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            675000,
            "4512 Mueller Blvd",
            "Austin",
            "TX",
            "78723",
            -97.7052,
            30.2988,
            3,
            2,
            1850,
            5500,
            2018,
            "Beautiful Mueller home with open floor plan, chef's kitchen, and private backyard. "
                + "Walking distance to parks, shops, and restaurants.",
            "https://picsum.photos/seed/prop003/300/200"));

    properties.add(
        new Property(
            "prop-004",
            PropertyType.HOUSE,
            PropertyStatus.PENDING,
            725000,
            "1809 Pecan Grove Ln",
            "Austin",
            "TX",
            "78723",
            -97.6985,
            30.3021,
            4,
            3,
            2200,
            6200,
            2020,
            "Spacious family home in desirable Mueller neighborhood. Features include smart home "
                + "technology, energy-efficient appliances, and two-car garage.",
            "https://picsum.photos/seed/prop004/300/200"));

    properties.add(
        new Property(
            "prop-022",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            425000,
            "1900 Aldrich St #204",
            "Austin",
            "TX",
            "78723",
            -97.7012,
            30.2965,
            2,
            2,
            1150,
            null,
            2019,
            "Contemporary Mueller condo with modern finishes. Open concept living, private patio, "
                + "and access to Thinkery and community amenities.",
            "https://picsum.photos/seed/prop022/300/200"));

    properties.add(
        new Property(
            "prop-023",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            549000,
            "5512 Berkman Dr",
            "Austin",
            "TX",
            "78723",
            -97.6923,
            30.3089,
            3,
            2,
            1650,
            5800,
            2015,
            "Charming East Austin home near Mueller. Updated throughout with modern kitchen, "
                + "hardwood floors, and lovely landscaped yard.",
            "https://picsum.photos/seed/prop023/300/200"));

    // ========== SOUTH AUSTIN / ZILKER ==========
    properties.add(
        new Property(
            "prop-005",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            895000,
            "2204 Barton Hills Dr",
            "Austin",
            "TX",
            "78704",
            -97.7823,
            30.2545,
            3,
            2,
            1650,
            8500,
            1972,
            "Classic Barton Hills charmer on a large lot. Updated kitchen and bathrooms while "
                + "maintaining mid-century character. Minutes to Zilker Park.",
            "https://picsum.photos/seed/prop005/300/200"));

    properties.add(
        new Property(
            "prop-006",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            445000,
            "1600 Barton Springs Rd #4301",
            "Austin",
            "TX",
            "78704",
            -97.7712,
            30.2612,
            2,
            2,
            1100,
            null,
            2008,
            "South Austin condo with hill country views. Open layout, granite counters, "
                + "and private balcony. Community pool and fitness center.",
            "https://picsum.photos/seed/prop006/300/200"));

    properties.add(
        new Property(
            "prop-024",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            1150000,
            "1801 Kinney Ave",
            "Austin",
            "TX",
            "78704",
            -97.7645,
            30.2498,
            4,
            3,
            2400,
            7200,
            2016,
            "Stunning modern home in Bouldin Creek. Designer finishes, chef's kitchen with island, "
                + "outdoor living space, and rooftop deck with downtown views.",
            "https://picsum.photos/seed/prop024/300/200"));

    properties.add(
        new Property(
            "prop-025",
            PropertyType.APARTMENT,
            PropertyStatus.FOR_SALE,
            375000,
            "2505 S Lamar Blvd #312",
            "Austin",
            "TX",
            "78704",
            -97.7789,
            30.2412,
            2,
            1,
            925,
            null,
            2017,
            "Stylish South Lamar apartment in walkable location. Near Alamo Drafthouse, "
                + "restaurants, and parks. Modern finishes and balcony.",
            "https://picsum.photos/seed/prop025/300/200"));

    properties.add(
        new Property(
            "prop-026",
            PropertyType.HOUSE,
            PropertyStatus.SOLD,
            785000,
            "2612 Kinney Oaks Ct",
            "Austin",
            "TX",
            "78704",
            -97.7756,
            30.2389,
            3,
            2,
            1800,
            6500,
            2010,
            "Recently sold! Popular Zilker neighborhood home with open layout and updated finishes. "
                + "Walking distance to Barton Springs Pool.",
            "https://picsum.photos/seed/prop026/300/200"));

    // ========== SOUTH CONGRESS (SOCO) ==========
    properties.add(
        new Property(
            "prop-016",
            PropertyType.APARTMENT,
            PropertyStatus.FOR_SALE,
            425000,
            "1801 S Congress Ave #302",
            "Austin",
            "TX",
            "78704",
            -97.7512,
            30.2456,
            2,
            1,
            950,
            null,
            2020,
            "Trendy SoCo living! Walk to iconic shops, restaurants, and entertainment. "
                + "Modern unit with balcony overlooking Congress Avenue.",
            "https://picsum.photos/seed/prop016/300/200"));

    properties.add(
        new Property(
            "prop-027",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            515000,
            "1620 S Congress Ave #410",
            "Austin",
            "TX",
            "78704",
            -97.7508,
            30.2512,
            2,
            2,
            1075,
            null,
            2019,
            "Upscale South Congress condo with premium finishes. Walk to local boutiques, "
                + "food trailers, and live music venues. Rooftop pool.",
            "https://picsum.photos/seed/prop027/300/200"));

    properties.add(
        new Property(
            "prop-028",
            PropertyType.COMMERCIAL,
            PropertyStatus.FOR_SALE,
            1650000,
            "1400 S Congress Ave",
            "Austin",
            "TX",
            "78704",
            -97.7505,
            30.2534,
            null,
            null,
            2800,
            4500,
            1960,
            "Prime South Congress retail space. High visibility on Austin's most iconic street. "
                + "Suitable for boutique, restaurant, or gallery.",
            "https://picsum.photos/seed/prop028/300/200"));

    // ========== NORTH AUSTIN / DOMAIN ==========
    properties.add(
        new Property(
            "prop-007",
            PropertyType.APARTMENT,
            PropertyStatus.FOR_SALE,
            295000,
            "11011 Domain Dr #1502",
            "Austin",
            "TX",
            "78758",
            -97.7234,
            30.3998,
            1,
            1,
            720,
            null,
            2017,
            "Modern Domain apartment with high-end finishes. Walk to shopping, dining, and "
                + "entertainment. Perfect for young professionals.",
            "https://picsum.photos/seed/prop007/300/200"));

    properties.add(
        new Property(
            "prop-008",
            PropertyType.COMMERCIAL,
            PropertyStatus.FOR_SALE,
            1250000,
            "9800 N Lamar Blvd",
            "Austin",
            "TX",
            "78753",
            -97.6912,
            30.3856,
            null,
            null,
            4500,
            12000,
            2005,
            "Prime retail space on busy N Lamar corridor. High visibility, ample parking, "
                + "and excellent traffic counts. Ideal for restaurant or retail.",
            "https://picsum.photos/seed/prop008/300/200"));

    properties.add(
        new Property(
            "prop-029",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            365000,
            "3600 N Lamar Blvd #401",
            "Austin",
            "TX",
            "78756",
            -97.7312,
            30.3123,
            2,
            2,
            1050,
            null,
            2020,
            "North Central Austin condo near Triangle. Walking distance to restaurants, shops, "
                + "and entertainment. Modern design with covered parking.",
            "https://picsum.photos/seed/prop029/300/200"));

    properties.add(
        new Property(
            "prop-030",
            PropertyType.APARTMENT,
            PropertyStatus.PENDING,
            345000,
            "10800 Pecan Park Blvd #2204",
            "Austin",
            "TX",
            "78758",
            -97.7145,
            30.4012,
            2,
            2,
            1100,
            null,
            2018,
            "Spacious Domain-area apartment with den. Resort-style pool, fitness center, and "
                + "dog park. Minutes to tech employers.",
            "https://picsum.photos/seed/prop030/300/200"));

    // ========== WEST AUSTIN / LAKE TRAVIS ==========
    properties.add(
        new Property(
            "prop-009",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            1850000,
            "5901 Bold Ruler Way",
            "Austin",
            "TX",
            "78746",
            -97.8234,
            30.3012,
            5,
            4,
            4200,
            21000,
            2015,
            "Luxurious estate in prestigious Barton Creek. Features include wine cellar, "
                + "home theater, infinity pool, and breathtaking hill country views.",
            "https://picsum.photos/seed/prop009/300/200"));

    properties.add(
        new Property(
            "prop-010",
            PropertyType.HOUSE,
            PropertyStatus.SOLD,
            985000,
            "3402 Westlake Dr",
            "Austin",
            "TX",
            "78746",
            -97.8012,
            30.2934,
            4,
            3,
            2800,
            15000,
            1998,
            "Recently sold! Beautiful Westlake home with pool and mature landscaping. "
                + "Excellent schools and convenient location.",
            "https://picsum.photos/seed/prop010/300/200"));

    properties.add(
        new Property(
            "prop-031",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            2450000,
            "4500 River Place Blvd",
            "Austin",
            "TX",
            "78730",
            -97.8456,
            30.3756,
            5,
            5,
            5100,
            35000,
            2018,
            "Magnificent River Place estate with Lake Austin views. Gourmet kitchen, home office, "
                + "guest quarters, and private boat dock access.",
            "https://picsum.photos/seed/prop031/300/200"));

    properties.add(
        new Property(
            "prop-032",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            1275000,
            "8912 Big View Dr",
            "Austin",
            "TX",
            "78730",
            -97.8312,
            30.3589,
            4,
            3,
            3200,
            18000,
            2012,
            "Hill Country contemporary with stunning views. Open floor plan, walls of windows, "
                + "saltwater pool, and outdoor kitchen.",
            "https://picsum.photos/seed/prop032/300/200"));

    properties.add(
        new Property(
            "prop-033",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            625000,
            "3801 N Capital of Texas Hwy #205",
            "Austin",
            "TX",
            "78746",
            -97.7923,
            30.3234,
            3,
            2,
            1650,
            null,
            2016,
            "Upscale Westlake condo with hill country views. Gated community, resort amenities, "
                + "and walking trails. Close to Barton Creek Mall.",
            "https://picsum.photos/seed/prop033/300/200"));

    // ========== ROUND ROCK ==========
    properties.add(
        new Property(
            "prop-011",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            465000,
            "1205 Brushy Creek Blvd",
            "Round Rock",
            "TX",
            "78681",
            -97.6792,
            30.5082,
            4,
            2,
            2100,
            7500,
            2012,
            "Fantastic Round Rock home near excellent schools. Open floor plan, updated "
                + "kitchen, covered patio, and community amenities.",
            "https://picsum.photos/seed/prop011/300/200"));

    properties.add(
        new Property(
            "prop-012",
            PropertyType.COMMERCIAL,
            PropertyStatus.FOR_SALE,
            875000,
            "200 E Main St",
            "Round Rock",
            "TX",
            "78664",
            -97.6789,
            30.5087,
            null,
            null,
            3200,
            8500,
            1985,
            "Historic downtown Round Rock commercial building. Recently renovated with "
                + "modern systems while preserving character. Multiple tenant spaces.",
            "https://picsum.photos/seed/prop012/300/200"));

    properties.add(
        new Property(
            "prop-034",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            525000,
            "2501 Arbor Dr",
            "Round Rock",
            "TX",
            "78681",
            -97.6856,
            30.5145,
            4,
            3,
            2450,
            8200,
            2017,
            "Beautiful Round Rock family home in sought-after neighborhood. Open concept, "
                + "game room, covered patio, and community pool access.",
            "https://picsum.photos/seed/prop034/300/200"));

    properties.add(
        new Property(
            "prop-035",
            PropertyType.HOUSE,
            PropertyStatus.PENDING,
            389000,
            "1612 Bayland St",
            "Round Rock",
            "TX",
            "78664",
            -97.6701,
            30.4978,
            3,
            2,
            1750,
            6000,
            2008,
            "Well-maintained Round Rock home near Dell campus. Updated kitchen, new flooring, "
                + "and spacious backyard. Great starter home!",
            "https://picsum.photos/seed/prop035/300/200"));

    properties.add(
        new Property(
            "prop-036",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            285000,
            "2800 La Frontera Blvd #304",
            "Round Rock",
            "TX",
            "78681",
            -97.6923,
            30.4823,
            2,
            2,
            1100,
            null,
            2015,
            "La Frontera condo with shopping and dining at your doorstep. Modern finishes, "
                + "open layout, and community pool. Near IKEA.",
            "https://picsum.photos/seed/prop036/300/200"));

    // ========== CEDAR PARK ==========
    properties.add(
        new Property(
            "prop-013",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            545000,
            "2801 Grand Oaks Loop",
            "Cedar Park",
            "TX",
            "78613",
            -97.8198,
            30.5054,
            4,
            3,
            2450,
            8000,
            2019,
            "Like-new Cedar Park home in master-planned community. Features include "
                + "gourmet kitchen, game room, and resort-style community amenities.",
            "https://picsum.photos/seed/prop013/300/200"));

    properties.add(
        new Property(
            "prop-014",
            PropertyType.CONDO,
            PropertyStatus.PENDING,
            275000,
            "1890 Ranch Rd 1431 #205",
            "Cedar Park",
            "TX",
            "78613",
            -97.8312,
            30.4923,
            2,
            2,
            1050,
            null,
            2016,
            "Move-in ready Cedar Park condo near shopping and dining. Open layout, "
                + "stainless appliances, and attached garage.",
            "https://picsum.photos/seed/prop014/300/200"));

    properties.add(
        new Property(
            "prop-037",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            485000,
            "1901 Little Elm Trail",
            "Cedar Park",
            "TX",
            "78613",
            -97.8089,
            30.5234,
            4,
            2,
            2200,
            7000,
            2014,
            "Spacious Cedar Park home with open floor plan and large backyard. Near parks, "
                + "trails, and excellent Leander ISD schools.",
            "https://picsum.photos/seed/prop037/300/200"));

    properties.add(
        new Property(
            "prop-038",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            625000,
            "3100 Crystal Falls Pkwy",
            "Cedar Park",
            "TX",
            "78613",
            -97.8534,
            30.5312,
            5,
            3,
            3100,
            9500,
            2020,
            "Stunning Crystal Falls home with lake views. Premium lot, covered patio with "
                + "outdoor kitchen, and access to community amenities.",
            "https://picsum.photos/seed/prop038/300/200"));

    // ========== PFLUGERVILLE ==========
    properties.add(
        new Property(
            "prop-015",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            385000,
            "19401 Moorlynch Ave",
            "Pflugerville",
            "TX",
            "78660",
            -97.6201,
            30.4512,
            3,
            2,
            1750,
            5500,
            2021,
            "Brand new construction in growing Pflugerville! Energy-efficient design, "
                + "modern finishes, and family-friendly neighborhood.",
            "https://picsum.photos/seed/prop015/300/200"));

    properties.add(
        new Property(
            "prop-039",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            425000,
            "20105 Weyland Dr",
            "Pflugerville",
            "TX",
            "78660",
            -97.6312,
            30.4623,
            4,
            2,
            2000,
            6500,
            2019,
            "Popular Blackhawk neighborhood home with open layout. Large kitchen island, "
                + "covered patio, and community pool access.",
            "https://picsum.photos/seed/prop039/300/200"));

    properties.add(
        new Property(
            "prop-040",
            PropertyType.HOUSE,
            PropertyStatus.SOLD,
            365000,
            "1501 Tranquility Ln",
            "Pflugerville",
            "TX",
            "78660",
            -97.6089,
            30.4389,
            3,
            2,
            1650,
            5200,
            2016,
            "Recently sold! Desirable Pflugerville location near Stone Hill Town Center. "
                + "Updated kitchen and great backyard.",
            "https://picsum.photos/seed/prop040/300/200"));

    // ========== NORTH LOOP / HYDE PARK ==========
    properties.add(
        new Property(
            "prop-018",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            595000,
            "5202 Avenue G",
            "Austin",
            "TX",
            "78751",
            -97.7234,
            30.3198,
            3,
            2,
            1450,
            6000,
            1955,
            "Charming North Loop bungalow with modern updates. Original hardwoods, "
                + "updated kitchen, and shaded backyard. Walk to local shops.",
            "https://picsum.photos/seed/prop018/300/200"));

    properties.add(
        new Property(
            "prop-041",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            725000,
            "4201 Avenue D",
            "Austin",
            "TX",
            "78751",
            -97.7289,
            30.3089,
            3,
            2,
            1680,
            7200,
            1948,
            "Stunning Hyde Park renovation with modern addition. Chef's kitchen, spa bath, "
                + "and separate studio/ADU. Walk to UT.",
            "https://picsum.photos/seed/prop041/300/200"));

    properties.add(
        new Property(
            "prop-042",
            PropertyType.APARTMENT,
            PropertyStatus.FOR_SALE,
            325000,
            "5000 Airport Blvd #105",
            "Austin",
            "TX",
            "78751",
            -97.7156,
            30.3145,
            1,
            1,
            750,
            null,
            2018,
            "Modern North Loop apartment near local favorites. Walk to Epoch Coffee, "
                + "Thunderbird, and North Loop shops. Pet friendly!",
            "https://picsum.photos/seed/prop042/300/200"));

    // ========== EAST RIVERSIDE ==========
    properties.add(
        new Property(
            "prop-017",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            315000,
            "1600 Barton Springs Rd #5102",
            "Austin",
            "TX",
            "78704",
            -97.7689,
            30.2598,
            1,
            1,
            780,
            null,
            2010,
            "Affordable Austin living with amazing amenities. Close to downtown, "
                + "Lady Bird Lake trails, and public transit.",
            "https://picsum.photos/seed/prop017/300/200"));

    properties.add(
        new Property(
            "prop-043",
            PropertyType.APARTMENT,
            PropertyStatus.FOR_SALE,
            285000,
            "2200 S Lakeshore Blvd #304",
            "Austin",
            "TX",
            "78741",
            -97.7234,
            30.2345,
            2,
            1,
            875,
            null,
            2019,
            "East Riverside apartment with lake views. Near Oracle campus and easy access "
                + "to downtown. Modern finishes and balcony.",
            "https://picsum.photos/seed/prop043/300/200"));

    properties.add(
        new Property(
            "prop-044",
            PropertyType.CONDO,
            PropertyStatus.PENDING,
            345000,
            "1801 E Riverside Dr #402",
            "Austin",
            "TX",
            "78741",
            -97.7312,
            30.2389,
            2,
            2,
            1025,
            null,
            2020,
            "New construction Riverside condo with downtown views. High-end finishes, "
                + "rooftop pool, and walkable to trails.",
            "https://picsum.photos/seed/prop044/300/200"));

    // ========== LAKEWAY / BEE CAVE ==========
    properties.add(
        new Property(
            "prop-045",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            1450000,
            "15301 Spillman Ranch Loop",
            "Austin",
            "TX",
            "78738",
            -97.9234,
            30.3234,
            5,
            4,
            4500,
            25000,
            2017,
            "Stunning Spanish Oaks estate on premium lot. Chef's kitchen, wine room, "
                + "pool/spa, and private access to hiking trails.",
            "https://picsum.photos/seed/prop045/300/200"));

    properties.add(
        new Property(
            "prop-046",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            875000,
            "5200 Bee Creek Rd",
            "Austin",
            "TX",
            "78738",
            -97.9012,
            30.3089,
            4,
            3,
            2800,
            12000,
            2015,
            "Hill Country home in Bee Cave with stunning views. Open floor plan, "
                + "outdoor living area, and highly-rated Lake Travis schools.",
            "https://picsum.photos/seed/prop046/300/200"));

    properties.add(
        new Property(
            "prop-047",
            PropertyType.CONDO,
            PropertyStatus.FOR_SALE,
            495000,
            "2611 Bee Cave Rd #201",
            "Austin",
            "TX",
            "78746",
            -97.8123,
            30.2978,
            2,
            2,
            1350,
            null,
            2019,
            "Luxury condo near Hill Country Galleria. High-end finishes, covered parking, "
                + "and resort-style community amenities.",
            "https://picsum.photos/seed/prop047/300/200"));

    // ========== SOUTH AUSTIN / MANCHACA ==========
    properties.add(
        new Property(
            "prop-048",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            475000,
            "9501 Bradshaw Rd",
            "Austin",
            "TX",
            "78748",
            -97.8234,
            30.1678,
            3,
            2,
            1800,
            8500,
            2010,
            "South Austin gem on large lot. Updated kitchen, hardwood floors, and "
                + "huge backyard perfect for entertaining.",
            "https://picsum.photos/seed/prop048/300/200"));

    properties.add(
        new Property(
            "prop-049",
            PropertyType.HOUSE,
            PropertyStatus.FOR_SALE,
            545000,
            "10401 Ivalenes Hope Dr",
            "Austin",
            "TX",
            "78748",
            -97.8089,
            30.1589,
            4,
            3,
            2300,
            7200,
            2018,
            "Beautiful Circle C home with open concept living. Gourmet kitchen, "
                + "covered patio, and access to amazing community amenities.",
            "https://picsum.photos/seed/prop049/300/200"));

    properties.add(
        new Property(
            "prop-050",
            PropertyType.HOUSE,
            PropertyStatus.PENDING,
            625000,
            "7900 Escala Dr",
            "Austin",
            "TX",
            "78735",
            -97.8567,
            30.2012,
            4,
            3,
            2650,
            9000,
            2019,
            "Gorgeous home in Covered Bridge at Oak Forest. Premium finishes, "
                + "private backyard, and excellent SW Austin location.",
            "https://picsum.photos/seed/prop050/300/200"));

    return properties;
  }
}
