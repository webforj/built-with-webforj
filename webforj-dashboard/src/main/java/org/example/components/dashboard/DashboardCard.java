package org.example.components.dashboard;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Dashboard card component focused solely on UI presentation.
 * This component displays title, price, percentage change, and a chart
 * without handling chart generation or data creation logic.
 */
public class DashboardCard extends Composite<FlexLayout> {

  private final FlexLayout self = getBoundComponent();
  private final FlexLayout textData = new FlexLayout();
  private final Paragraph title = new Paragraph();
  private final Paragraph price = new Paragraph();
  private final Paragraph percentChange = new Paragraph();
  private final FlexLayout numericData = new FlexLayout(price, percentChange);
  private final FlexLayout mainText = new FlexLayout(title, numericData);
  private final Button followButton = new Button("Follow");
  private final Paragraph details = new Paragraph();
  private final FlexLayout detailText = new FlexLayout(followButton, details);
  private GoogleChart chart;
  
  private double percentage = 0;
  private boolean isFollowing = false;

  /**
   * Creates an empty dashboard card without any chart.
   * Use setChart() to add a chart after creation.
   */
  public DashboardCard() {
    initComponent();
  }

  /**
   * Creates a dashboard card with the provided chart.
   * 
   * @param chart The GoogleChart to display in the card
   */
  public DashboardCard(GoogleChart chart) {
    this.chart = chart;
    initComponent();
  }

  /**
   * Creates a dashboard card with data and a chart.
   * 
   * @param title The title to display
   * @param price The price value to display
   * @param percent The percentage change
   * @param chart The GoogleChart to display
   */
  public DashboardCard(String title, double price, double percent, GoogleChart chart) {
    this.chart = chart;
    this.percentage = percent;
    
    setCardData(title, price, percent);
    initComponent();
  }

  /**
   * Sets the card data (title, price, percentage).
   * 
   * @param title The title to display
   * @param price The price value
   * @param percent The percentage change
   */
  public void setCardData(String title, double price, double percent) {
    this.title.setText(title);
    this.price.setText(formatValue(title, price));
    this.percentage = percent;
    
    // Format percentage with proper sign
    this.percentChange.setText((percent >= 0 ? "+" : "") + String.format("%.2f", percent) + "%");
    
    // Update timestamp
    LocalDateTime now = LocalDateTime.now();
    String date = now.format(DateTimeFormatter.ofPattern("MMM d"));
    String time = now.format(DateTimeFormatter.ofPattern("h:mm a"));
    this.details.setHtml("<span style='color: var(--dwc-color-gray-40);'>Last updated: </span>" + date + " • " + time);
    
    // Update percentage styling
    updatePercentageStyle();
  }
  
  /**
   * Sets the chart for this card.
   * 
   * @param chart The GoogleChart to display
   */
  public void setChart(GoogleChart chart) {
    if (this.chart != null) {
      self.remove(this.chart);
    }
    this.chart = chart;
    if (chart != null) {
      self.add(chart);
    }
  }

  private String formatValue(String title, double value) {
    if (title.contains("Dominance")) {
      return FormatUtils.formatPercentage(value);
    } else {
      return FormatUtils.formatLargeNumber(value);
    }
  }

  private void initComponent() {
    // Layout setup
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("gap", "0px");
    
    // Add text data section
    self.add(textData);
    
    // Add chart if present
    if (chart != null) {
      self.add(chart);
    }
    
    // Configure layouts
    textData.setJustifyContent(FlexJustifyContent.BETWEEN);
    textData.setStyle("min-height", "80px");
    textData.add(mainText, detailText);
    mainText.setDirection(FlexDirection.COLUMN).setStyle("gap", "0px");
    detailText.setDirection(FlexDirection.COLUMN);
    numericData.setAlignment(FlexAlignment.CENTER);
    
    // Apply CSS classes
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

    // Update percentage styling
    updatePercentageStyle();
  }
  
  private void updatePercentageStyle() {
    if (Double.compare(percentage, 0.0) > 0) {
      percentChange.addClassName("percentage-positive");
      percentChange.removeClassName("percentage-negative");
    } else {
      percentChange.addClassName("percentage-negative");
      percentChange.removeClassName("percentage-positive");
    }
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
  
  /**
   * Gets the GoogleChart instance for this card.
   * 
   * @return the chart component, or null if no chart is set
   */
  public GoogleChart getChart() {
    return chart;
  }
}
