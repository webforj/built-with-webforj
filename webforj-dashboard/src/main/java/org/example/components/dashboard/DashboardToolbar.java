package org.example.components.dashboard;

import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;

/**
 * Dashboard toolbar component with time range dropdown and interval tabs.
 * Styled to match DexScreener's design with primary colors.
 */
public class DashboardToolbar extends Composite<FlexLayout> {

  private final FlexLayout self = getBoundComponent();
  private ChoiceBox timeRangeDropdown;
  private TabbedPane intervalTabs;
  private FlexLayout intervalContainer;

  public DashboardToolbar() {
    initializeComponent();
  }

  private void initializeComponent() {
    self.addClassName("dashboard-toolbar")
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-s)")
        .setPadding("var(--dwc-space-xs)");

    createTimeRangeDropdown();
    createIntervalTabs();

    self.add(timeRangeDropdown, intervalContainer);
  }

  private void createTimeRangeDropdown() {
    timeRangeDropdown = new ChoiceBox();
    timeRangeDropdown.addClassName("dashboard-toolbar__dropdown");

    // Add time range options
    timeRangeDropdown.add("Last 30 mins");
    timeRangeDropdown.add("Last 6 hours");
    timeRangeDropdown.add("Last 12 hours");
    timeRangeDropdown.add("Last 24 hours");
    timeRangeDropdown.add("Last 3 days");
    timeRangeDropdown.add("Last 7 days");

    // Set default selection by index (3rd item = "Last 24 hours")
    timeRangeDropdown.selectIndex(3);
    timeRangeDropdown.setPrefixComponent(TablerIcon.create("clock"));
    timeRangeDropdown.setTheme(ButtonTheme.PRIMARY);
  }

  private void createIntervalTabs() {
    // Create container with flame icon and tabs
    intervalContainer = new FlexLayout();
    intervalContainer.addClassName("dashboard-toolbar__interval-container")
        .setAlignment(FlexAlignment.CENTER)
        .setSpacing("0px");

    // Add flame icon
    Icon flameIcon = TablerIcon.create("flame");
    flameIcon.addClassName("dashboard-toolbar__flame-icon");

    // Create tabbed pane
    intervalTabs = new TabbedPane();
    intervalTabs.addClassName("dashboard-toolbar__interval-tabs");

    // Create interval tabs without body content
    String[] intervals = { "5m", "1h", "6h", "24h"};

    for (String interval : intervals) {
      intervalTabs.addTab(interval);
    }
    intervalTabs.setTheme(Theme.PRIMARY)
        .setHideActiveIndicator(true)
        .setBorderless(true);

    // Add icon and tabs to container
    intervalContainer.add(flameIcon, intervalTabs);
  }

  /**
   * Gets the currently selected time range.
   * 
   * @return the selected time range key
   */
  public String getSelectedTimeRange() {
    return timeRangeDropdown.getSelectedKey().toString();
  }
}