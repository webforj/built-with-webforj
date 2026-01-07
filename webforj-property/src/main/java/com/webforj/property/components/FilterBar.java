package com.webforj.property.components;

import com.webforj.component.Composite;
import com.webforj.component.element.event.ElementClickEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Img;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.event.ListSelectEvent;
import com.webforj.data.event.ValueChangeEvent;
import com.webforj.property.model.FilterState;
import com.webforj.property.model.PropertyType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Top filter bar containing search and filter controls.
 *
 * <p>Provides search field, property type dropdown, beds/baths filters, and filter chips for active
 * filters.
 */
public class FilterBar extends Composite<FlexLayout> {

  private final FlexLayout self = getBoundComponent();
  private final FilterState filterState = new FilterState();
  private final List<Consumer<FilterState>> filterChangeListeners = new ArrayList<>();

  private TextField searchField;
  private ChoiceBox typeChoice;
  private ChoiceBox bedsChoice;
  private ChoiceBox bathsChoice;

  private final Div chipsRow;
  private final Div resetAllBtn;
  private final FilterChip searchChip;
  private final FilterChip typeChip;
  private final FilterChip bedsChip;
  private final FilterChip bathsChip;

  /** Creates the filter bar with branding and filter controls. */
  public FilterBar() {
    self.setAlignment(FlexAlignment.CENTER);
    self.setSpacing("var(--dwc-space-l)");
    self.addClassName("filter-bar");

    chipsRow = new Div();
    chipsRow.addClassName("filter-bar__chips-row");

    resetAllBtn = new Div();
    resetAllBtn.addClassName("filter-bar__reset-all");
    resetAllBtn.setText("✕ Clear all");
    resetAllBtn.setStyle("display", "none");
    resetAllBtn.onClick(this::handleResetAllClick);

    searchChip = new FilterChip(this::handleSearchChipRemove);
    typeChip = new FilterChip(this::handleTypeChipRemove);
    bedsChip = new FilterChip(this::handleBedsChipRemove);
    bathsChip = new FilterChip(this::handleBathsChipRemove);

    chipsRow.add(
        resetAllBtn,
        searchChip.getContainer(),
        typeChip.getContainer(),
        bedsChip.getContainer(),
        bathsChip.getContainer());

    FlexLayout filtersWrapper =
        FlexLayout.create(createFilters(), chipsRow)
            .vertical()
            .build()
            .setSpacing("var(--dwc-space-xs)");
    filtersWrapper.addClassName("filter-bar__filters-wrapper");

    self.add(createBrand(), filtersWrapper);
  }

  private Div createBrand() {
    Div brand = new Div();
    brand.addClassName("filter-bar__brand");

    Div logoIcon = new Div();
    logoIcon.addClassName("filter-bar__logo-icon");
    Img logoImg = new Img("icons://logo.svg");
    logoImg.addClassName("filter-bar__logo-img");
    logoIcon.add(logoImg);

    Span brandText = new Span();
    brandText.addClassName("filter-bar__brand-text");
    Span brandPrimary = new Span("Austin");
    brandPrimary.addClassName("filter-bar__brand-primary");
    Span brandSecondary = new Span("Homes");
    brandSecondary.addClassName("filter-bar__brand-secondary");
    brandText.add(brandPrimary, brandSecondary);

    brand.add(logoIcon, brandText);
    return brand;
  }

  private FlexLayout createFilters() {
    searchField = new TextField();
    searchField.setPlaceholder("Search address...");
    searchField.onValueChange(this::handleSearchValueChange);

    typeChoice = new ChoiceBox();
    typeChoice.addClassName("filter-bar__type");
    typeChoice.add("ALL", "All Types");
    for (PropertyType type : PropertyType.values()) {
      typeChoice.add(type.name(), type.getDisplayName());
    }
    typeChoice.selectIndex(0);
    typeChoice.onSelect(this::handleTypeSelect);

    bedsChoice = new ChoiceBox();
    bedsChoice.addClassName("filter-bar__beds");
    bedsChoice.add("ANY", "Any Beds");
    bedsChoice.add("1", "1+ Bed");
    bedsChoice.add("2", "2+ Beds");
    bedsChoice.add("3", "3+ Beds");
    bedsChoice.add("4", "4+ Beds");
    bedsChoice.add("5", "5+ Beds");
    bedsChoice.selectIndex(0);
    bedsChoice.onSelect(this::handleBedsSelect);

    bathsChoice = new ChoiceBox();
    bathsChoice.addClassName("filter-bar__baths");
    bathsChoice.add("ANY", "Any Baths");
    bathsChoice.add("1", "1+ Bath");
    bathsChoice.add("2", "2+ Baths");
    bathsChoice.add("3", "3+ Baths");
    bathsChoice.add("4", "4+ Baths");
    bathsChoice.selectIndex(0);
    bathsChoice.onSelect(this::handleBathsSelect);

    FlexLayout filters =
        FlexLayout.create(searchField, typeChoice, bedsChoice, bathsChoice)
            .horizontal()
            .align()
            .center()
            .build()
            .setSpacing("var(--dwc-space-m)");
    filters.addClassName("filter-bar__filters");
    return filters;
  }

  private void handleSearchValueChange(ValueChangeEvent<String> event) {
    filterState.setSearchText(event.getValue());
    notifyFilterChange();
    updateChips();
  }

  private void handleTypeSelect(ListSelectEvent<Object> event) {
    Object key = event.getSelectedItem().getKey();
    if ("ALL".equals(key)) {
      filterState.setPropertyType(null);
    } else {
      filterState.setPropertyType(PropertyType.valueOf((String) key));
    }
    notifyFilterChange();
    updateChips();
  }

  private void handleBedsSelect(ListSelectEvent<Object> event) {
    Object key = event.getSelectedItem().getKey();
    if ("ANY".equals(key)) {
      filterState.setMinBeds(null);
    } else {
      filterState.setMinBeds(Integer.parseInt((String) key));
    }
    notifyFilterChange();
    updateChips();
  }

  private void handleBathsSelect(ListSelectEvent<Object> event) {
    Object key = event.getSelectedItem().getKey();
    if ("ANY".equals(key)) {
      filterState.setMinBaths(null);
    } else {
      filterState.setMinBaths(Integer.parseInt((String) key));
    }
    notifyFilterChange();
    updateChips();
  }

  private void handleResetAllClick(ElementClickEvent<?> event) {
    resetFilters();
  }

  private void handleSearchChipRemove(ElementClickEvent<?> event) {
    filterState.setSearchText("");
    searchField.setValue("");
    notifyFilterChange();
    updateChips();
  }

  private void handleTypeChipRemove(ElementClickEvent<?> event) {
    filterState.setPropertyType(null);
    typeChoice.selectIndex(0);
    notifyFilterChange();
    updateChips();
  }

  private void handleBedsChipRemove(ElementClickEvent<?> event) {
    filterState.setMinBeds(null);
    bedsChoice.selectIndex(0);
    notifyFilterChange();
    updateChips();
  }

  private void handleBathsChipRemove(ElementClickEvent<?> event) {
    filterState.setMinBaths(null);
    bathsChoice.selectIndex(0);
    notifyFilterChange();
    updateChips();
  }

  private void updateChips() {
    boolean hasFilters = filterState.hasActiveFilters();

    if (hasFilters) {
      chipsRow.addClassName("filter-bar__chips-row--active");
    } else {
      chipsRow.removeClassName("filter-bar__chips-row--active");
    }

    resetAllBtn.setStyle("display", hasFilters ? "flex" : "none");

    updateChip(
        searchChip,
        !filterState.getSearchText().isEmpty(),
        "\"" + filterState.getSearchText() + "\"");
    updateChip(
        typeChip,
        filterState.getPropertyType() != null,
        filterState.getPropertyType() != null
            ? "Type: " + filterState.getPropertyType().getDisplayName()
            : "");
    updateChip(
        bedsChip,
        filterState.getMinBeds() != null,
        filterState.getMinBeds() != null ? filterState.getMinBeds() + "+ Beds" : "");
    updateChip(
        bathsChip,
        filterState.getMinBaths() != null,
        filterState.getMinBaths() != null ? filterState.getMinBaths() + "+ Baths" : "");
  }

  private void updateChip(FilterChip chip, boolean visible, String text) {
    if (visible) {
      chip.show(text);
    } else {
      chip.hide();
    }
  }

  /**
   * Adds a listener for filter state changes.
   *
   * @param listener the listener to invoke with the new filter state
   */
  public void addFilterChangeListener(Consumer<FilterState> listener) {
    filterChangeListeners.add(listener);
  }

  /** Resets all filters to their default state. */
  public void resetFilters() {
    filterState.reset();
    searchField.setValue("");
    typeChoice.selectIndex(0);
    bedsChoice.selectIndex(0);
    bathsChoice.selectIndex(0);
    notifyFilterChange();
    updateChips();
  }

  /** Notifies listeners that the filter state has changed. */
  private void notifyFilterChange() {
    filterChangeListeners.forEach(listener -> listener.accept(filterState));
  }

  /**
   * Sets the enabled state of all filter controls.
   *
   * @param enabled true to enable filters, false to disable
   */
  public void setFiltersEnabled(boolean enabled) {
    searchField.setEnabled(enabled);
    typeChoice.setEnabled(enabled);
    bedsChoice.setEnabled(enabled);
    bathsChoice.setEnabled(enabled);
    chipsRow.setVisible(enabled);
  }
}
