package org.example.components;

import com.webforj.App;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
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
  
  GoogleChart chart = new GoogleChart(GoogleChart.Type.AREA);

  public DashboardCard() {
    initComponent();
  }

  public DashboardCard(String title, double price, double percent, String detailText) {
    this.title.setText(title);
    this.price.setText(formatValue(title, price));
    this.percentChange.setText((percent >= 0 ? "+" : "") + String.format("%.2f", percent) + "%");
    this.percentage = percent;
    
    // Set "Last updated" with current date and timestamp
    LocalDateTime now = LocalDateTime.now();
    String date = now.format(DateTimeFormatter.ofPattern("MMM d"));
    String time = now.format(DateTimeFormatter.ofPattern("h:mm a"));
    this.details.setHtml("<span style='color: rgb(128, 128, 128);'>Last updated: </span>" + date + " • " + time);
    
    initComponent();
  }
  
  private String formatValue(String title, double value) {
    if (title.contains("Dominance")) {
      return String.format("%.1f%%", value);
    } else if (value >= 1_000_000_000_000L) {
      return String.format("$%.2fT", value / 1_000_000_000_000.0);
    } else if (value >= 1_000_000_000) {
      return String.format("$%.1fB", value / 1_000_000_000.0);
    } else if (value >= 1_000_000) {
      return String.format("$%.1fM", value / 1_000_000.0);
    } else {
      return String.format("$%.2f", value);
    }
  }

  private void initComponent() {
    self.setDirection(FlexDirection.COLUMN);
    self.setSpacing("0px");
    self.add(textData, chart);
    textData.setJustifyContent(FlexJustifyContent.BETWEEN);
    textData.setStyle("min-height", "80px");
    textData.add(mainText, detailText);
    mainText.setDirection(FlexDirection.COLUMN).setSpacing("0px");
    detailText.setDirection(FlexDirection.COLUMN);
    numericData.setAlignment(FlexAlignment.CENTER);
    self.addClassName("data-card");
    title.addClassName("data-card__title");
    price.addClassName("data-card__price");
    percentChange.addClassName("data-card__percent");
    followButton.addClassName("data-card__follow-button")
        .setPrefixComponent(new TablerIcon().create("plus"))
        .setTheme(ButtonTheme.INFO);
    details.addClassName("data-card__details");
    
    // Add click event handler for follow/unfollow functionality
    followButton.addClickListener(e -> toggleFollow());

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
        "height", "100%"
    ));
    
    // Hide axes for a clean look
    options.put("hAxis", Map.of(
        "textPosition", "none",
        "gridlines", Map.of("color", "light-gray"),
        "baselineColor", "light-gray"
    ));
    options.put("vAxis", Map.of(
        "textPosition", "none",
        "gridlines", Map.of("color", "light-gray"),
        "baselineColor", "light-gray"
    ));
    
    // Area chart specific options
    options.put("areaOpacity", 0.3);
    options.put("lineWidth", 2);
    options.put("pointSize", 0);
    options.put("tooltip", Map.of("trigger", "none"));
    
    // Animation
    options.put("animation", Map.of(
        "startup", true,
        "duration", 1500,
        "easing", "out"
    ));
    
    chart.setOptions(options);
    
    // Generate and set data
    List<Object> data = generateChartData();
    chart.setData(data);
  }
  
  private List<Object> generateChartData() {
    List<Object> data = new ArrayList<>();
    // Header row
    data.add(Arrays.asList("Time", "Value"));
    
    // Generate 20 data points with more realistic variability
    Random random = new Random();
    double baseValue = 100;
    double trendFactor = percentage >= 0 ? 1.005 : 0.995; // Smaller trend for more natural look
    double momentum = 0; // Add momentum for more realistic movement
    
    for (int i = 0; i < 20; i++) {
      // Larger variation range and momentum-based movement
      double randomChange = (random.nextDouble() - 0.5) * 15; // Increased from 5 to 15
      momentum = momentum * 0.7 + randomChange * 0.3; // Smooth transitions
      
      // Add occasional spikes for more realistic market behavior
      if (random.nextDouble() < 0.1) { // 10% chance of spike
        momentum += (random.nextDouble() - 0.5) * 20;
      }
      
      double value = baseValue + momentum;
      data.add(Arrays.asList(String.valueOf(i), Math.max(value, 10))); // Ensure positive values
      
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
      followButton.setPrefixComponent(new TablerIcon().create("check"));
      followButton.setTheme(ButtonTheme.OUTLINED_INFO);
      
      // Show following toast
      Toast.show("Following " + title.getText(), Theme.SUCCESS);
    } else {
      // Change back to follow state
      followButton.setText("Follow");
      followButton.setPrefixComponent(new TablerIcon().create("plus"));
      followButton.setTheme(ButtonTheme.INFO);
      
      // Show unfollowing toast
      Toast.show("Unfollowed " + title.getText(), Theme.DEFAULT);
    }
  }
}
