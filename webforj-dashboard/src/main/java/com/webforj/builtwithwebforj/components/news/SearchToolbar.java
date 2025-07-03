package com.webforj.builtwithwebforj.components.news;

import com.webforj.component.Composite;
import com.webforj.component.Expanse;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.list.ChoiceBox;

public class SearchToolbar extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private TextField searchField;
  private ChoiceBox categoryFilter;
  private ChoiceBox timeFilter;
  private Runnable onSearchCallback;
  private Runnable onFilterChangeCallback;

  // Category constants
  private static final String CATEGORY_ALL = "All Categories";
  private static final String CATEGORY_BITCOIN = "Bitcoin";
  private static final String CATEGORY_ETHEREUM = "Ethereum";
  private static final String CATEGORY_DEFI = "DeFi";
  private static final String CATEGORY_REGULATION = "Regulation";
  private static final String CATEGORY_TECHNOLOGY = "Technology";

  // Time filter constants
  private static final String TIME_TODAY = "Today";
  private static final String TIME_WEEK = "This Week";
  private static final String TIME_MONTH = "This Month";
  private static final String TIME_ALL = "All Time";

  public SearchToolbar() {
    self.addClassName("news-view__toolbar")
        .setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-m)");

    createLeftSection();
    createRightSection();
  }

  private void createLeftSection() {
    FlexLayout leftSection = new FlexLayout();
    leftSection.addClassName("news-view__toolbar-left")
        .setAlignment(FlexAlignment.CENTER)
        .setSpacing("var(--dwc-space-m)");

    searchField = new TextField();
    searchField.addClassName("news-view__search");
    searchField.setPlaceholder("Search news articles...");
    searchField.setPrefixComponent(TablerIcon.create("search"));

    // Add search button to suffix
    Button searchButton = new Button();
    searchButton.setIcon(TablerIcon.create("arrow-right"));
    searchButton.setTheme(ButtonTheme.PRIMARY);
    searchButton.onClick(e -> {
      if (onSearchCallback != null) {
        onSearchCallback.run();
      }
    });
    searchField.setSuffixComponent(searchButton);

    categoryFilter = new ChoiceBox();
    categoryFilter.addClassName("news-view__filter");
    categoryFilter.add(CATEGORY_ALL);
    categoryFilter.add(CATEGORY_BITCOIN);
    categoryFilter.add(CATEGORY_ETHEREUM);
    categoryFilter.add(CATEGORY_DEFI);
    categoryFilter.add(CATEGORY_REGULATION);
    categoryFilter.add(CATEGORY_TECHNOLOGY);
    categoryFilter.setValue(CATEGORY_ALL);
    categoryFilter.onSelect(e -> {
      if (onFilterChangeCallback != null) {
        onFilterChangeCallback.run();
      }
    });

    timeFilter = new ChoiceBox();
    timeFilter.addClassName("news-view__filter news-view__filter--time");
    timeFilter.add(TIME_TODAY);
    timeFilter.add(TIME_WEEK);
    timeFilter.add(TIME_MONTH);
    timeFilter.add(TIME_ALL);
    timeFilter.setValue(TIME_TODAY);
    timeFilter.onSelect(e -> {
      if (onFilterChangeCallback != null) {
        onFilterChangeCallback.run();
      }
    });

    leftSection.add(searchField, categoryFilter, timeFilter);
    self.add(leftSection);
  }

  private void createRightSection() {
    FlexLayout rightSection = new FlexLayout();
    rightSection.addClassName("news-view__toolbar-right")
        .setAlignment(FlexAlignment.CENTER)
        .setSpacing("var(--dwc-space-s)");

    // Sort button
    Button sortBtn = new Button("Sort");
    sortBtn.setPrefixComponent(TablerIcon.create("arrows-sort"));
    sortBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    sortBtn.setExpanse(Expanse.NONE);

    // Export button
    Button exportBtn = new Button();
    exportBtn.setIcon(TablerIcon.create("download"));
    exportBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);

    // Bookmark button
    Button bookmarkBtn = new Button();
    bookmarkBtn.setIcon(TablerIcon.create("bookmark"));
    bookmarkBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);

    Span viewLabel = new Span("View:");
    viewLabel.addClassName("news-view__view-label");

    Button gridViewBtn = new Button();
    gridViewBtn.addClassName("news-view__view-btn");
    gridViewBtn.setIcon(TablerIcon.create("grid-dots"));
    gridViewBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);

    Button listViewBtn = new Button();
    listViewBtn.addClassName("news-view__view-btn");
    listViewBtn.setIcon(TablerIcon.create("list"));
    listViewBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);

    rightSection.add(sortBtn, exportBtn, bookmarkBtn, viewLabel, gridViewBtn, listViewBtn);
    self.add(rightSection);
  }

  public String getSearchTerm() {
    return searchField.getValue();
  }

  public String getSelectedCategory() {
    return categoryFilter.getSelectedItem() != null ? categoryFilter.getSelectedItem().getText() : CATEGORY_ALL;
  }

  public String getSelectedTimeFilter() {
    return timeFilter.getSelectedItem() != null ? timeFilter.getSelectedItem().getText() : TIME_TODAY;
  }

  public void setOnSearchCallback(Runnable callback) {
    this.onSearchCallback = callback;
  }

  public void setOnFilterChangeCallback(Runnable callback) {
    this.onFilterChangeCallback = callback;
  }
}