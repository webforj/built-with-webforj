package org.example.components;

import org.example.utils.FormatUtils;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.googlecharts.GoogleChart;
import com.webforj.component.toast.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardCard extends Composite<FlexLayout> {

  FlexLayout self = getBoundComponent();
  FlexLayout textData = new FlexLayout();

  double percentage = 0;

  Paragraph title = new Paragraph();
  Paragraph price = new Paragraph();
  Paragraph percentChange = new Paragraph();
  FlexLayout numericData = new FlexLayout(price, percentChange);
  FlexLayout mainText = new FlexLayout(title, numericData);

  Button followButton = new Button("Follow");
  Paragraph details = new Paragraph();
  FlexLayout detailText = new FlexLayout(followButton, details);
  private boolean isFollowing = false;

  GoogleChart chart;
  private GoogleChart.Type chartType = GoogleChart.Type.AREA;

  Random random = new Random();

  public DashboardCard() {
    this.chart = new GoogleChart(this.chartType);
    initComponent();
  }

  public DashboardCard(GoogleChart.Type chartType) {
    this.chartType = chartType;
    this.chart = new GoogleChart(this.chartType);
    initComponent();
  }

  public DashboardCard(String title, double price, double percent) {
    this(title, price, percent, GoogleChart.Type.AREA);
  }

  public DashboardCard(String title, double price, double percent, GoogleChart.Type chartType) {
    this.chartType = chartType;
    this.chart = new GoogleChart(this.chartType);
    this.title.setText(title);
    this.price.setText(formatValue(title, price));
    this.percentChange.setText((percent >= 0 ? "+" : "") + String.format("%.2f", percent) + "%");
    this.percentage = percent;

    LocalDateTime now = LocalDateTime.now();
    String date = now.format(DateTimeFormatter.ofPattern("MMM d"));
    String time = now.format(DateTimeFormatter.ofPattern("h:mm a"));
    this.details.setHtml("<span style='color: var(--dwc-color-gray-40);'>Last updated: </span>" + date + " • " + time);

    initComponent();
  }

  private String formatValue(String title, double value) {
    if (title.contains("Dominance")) {
      return FormatUtils.formatPercentage(value);
    } else {
      return FormatUtils.formatLargeNumber(value);
    }
  }

  private void initComponent() {
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("gap", "0px");
    self.add(textData, chart);
    textData.setJustifyContent(FlexJustifyContent.BETWEEN);
    textData.setStyle("min-height", "80px");
    textData.add(mainText, detailText);
    mainText.setDirection(FlexDirection.COLUMN).setStyle("gap", "0px");
    detailText.setDirection(FlexDirection.COLUMN);
    numericData.setAlignment(FlexAlignment.CENTER);
    self.addClassName("data-card");
    title.addClassName("data-card__title");
    price.addClassName("data-card__price");
    percentChange.addClassName("data-card__percent");
    followButton.addClassName("data-card__follow-button")
        .setPrefixComponent(TablerIcon.create("plus"))
        .setTheme(ButtonTheme.DEFAULT);
    details.addClassName("data-card__details");

    // Add click event handler for follow/unfollow functionality
    followButton.onClick(e -> toggleFollow());

    if (Double.compare(percentage, 0.0) > 0) {
      percentChange.addClassName("percentage-positive");
      percentChange.removeClassName("percentage-negative");
    } else {
      percentChange.addClassName("percentage-negative");
      percentChange.removeClassName("percentage-positive");
    }

    // Configure the chart
    configureChart();
  }

  private void configureChart() {
    chart.setHeight("100px");
    chart.setWidth("100%");

    // Configure chart options
    Map<String, Object> options = new HashMap<>();
    String color = percentage >= 0 ? "#22c55e" : "#ef4444";
    options.put("colors", List.of(color));
    options.put("backgroundColor", "transparent");
    options.put("legend", "none");

    // Configure chart area to fill the space
    options.put("chartArea", Map.of(
        "left", 0,
        "top", 0,
        "width", "100%",
        "height", "100%"));

    // Hide axes labels but show light gray grid lines
    options.put("hAxis", Map.of(
        "textPosition", "none",
        "gridlines", Map.of("color", "var(--dwc-color-gray-20)"),
        "baselineColor", "var(--dwc-color-gray-20)"));
    options.put("vAxis", Map.of(
        "textPosition", "none",
        "gridlines", Map.of("color", "var(--dwc-color-gray-20)"),
        "baselineColor", "var(--dwc-color-gray-20)"));

    // Chart type specific options
    switch (chartType) {
      case AREA:
        options.put("areaOpacity", 0.3);
        options.put("lineWidth", 2);
        options.put("pointSize", 0);
        break;
      case LINE:
        options.put("lineWidth", 3);
        options.put("pointSize", 0);
        options.put("curveType", "function");
        break;
      case COLUMN:
        options.put("bar", Map.of("groupWidth", "80%"));
        break;
      case SCATTER:
        options.put("pointSize", 8);
        options.put("pointShape", "circle");
        options.put("trendlines", Map.of(
            "0", Map.of(
                "type", "linear",
                "color", color,
                "lineWidth", 2,
                "opacity", 0.3,
                "showR2", false,
                "visibleInLegend", false)));

        break;
      default:
        break;
    }

    options.put("tooltip", Map.of("trigger", "none"));

    // Animation
    options.put("animation", Map.of(
        "startup", true,
        "duration", 1500,
        "easing", "out"));

    chart.setOptions(options);

    // Generate and set data
    List<Object> data = generateChartData();
    chart.setData(data);
  }

  private List<Object> generateChartData() {
    List<Object> data = new ArrayList<>();
    // Header row
    data.add(Arrays.asList("Time", "Value"));

    // Generate data points based on chart type
    double baseValue = 100;
    double trendFactor = percentage >= 0 ? 1.005 : 0.995;
    double momentum = 0;

    int dataPoints;
    switch (chartType) {
      case GoogleChart.Type.COLUMN -> dataPoints = 10;
      case GoogleChart.Type.SCATTER -> dataPoints = 30;
      default -> dataPoints = 20;
    }

    for (int i = 0; i < dataPoints; i++) {
      String label;
      if (chartType == GoogleChart.Type.COLUMN) {
        // Use time labels for column charts (e.g., "12PM", "1PM", etc.)
        label = String.format("%d:00", (i * 2 + 8) % 24);
      } else {
        label = String.valueOf(i);
      }

      // Generate realistic market movement
      double randomChange = (random.nextDouble() - 0.5) * 15;
      momentum = momentum * 0.7 + randomChange * 0.3;

      // Add occasional spikes
      if (random.nextDouble() < 0.1) {
        momentum += (random.nextDouble() - 0.5) * 20;
      }

      double value = baseValue + momentum;

      // For scatter charts, add more variation to create a cloud effect
      if (chartType == GoogleChart.Type.SCATTER) {
        value += (random.nextDouble() - 0.5) * 10; // Additional scatter
      }

      data.add(Arrays.asList(label, Math.max(value, 10)));

      // Apply trend with some randomness
      baseValue *= trendFactor * (0.98 + random.nextDouble() * 0.04);
    }

    return data;
  }

  private void toggleFollow() {
    isFollowing = !isFollowing;

    if (isFollowing) {
      // Change to unfollow state
      followButton.setText("Unfollow");
      followButton.setPrefixComponent(TablerIcon.create("check"));
      followButton.setTheme(ButtonTheme.OUTLINED_DEFAULT);

      // Show following toast
      Toast.show("Following " + title.getText());
    } else {
      // Change back to follow state
      followButton.setText("Follow");
      followButton.setPrefixComponent(TablerIcon.create("plus"));
      followButton.setTheme(ButtonTheme.DEFAULT);

      // Show unfollowing toast
      Toast.show("Unfollowed " + title.getText());
    }
  }
}
