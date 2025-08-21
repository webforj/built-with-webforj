package com.webforj.builtwithwebforj.todo.components;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import java.util.function.Consumer;

public class TodoFooter extends Composite<FlexLayout> {

    public enum FilterType {
        ALL("All"),
        ACTIVE("Active"),
        COMPLETED("Completed");
        
        private final String label;
        
        FilterType(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }

    private Span itemCount;
    private FlexLayout filterButtons;
    private Button allButton;
    private Button activeButton;
    private Button completedButton;
    private FilterType currentFilter = FilterType.ALL;
    private Consumer<FilterType> onFilterChange;

    /**
     * Constructs a new TodoFooter component.
     */
    public TodoFooter() {
        // Initialize components
        itemCount = new Span("0 items");
        itemCount.addClassName("todo-footer-count");

        // Create filter buttons
        allButton = new Button(FilterType.ALL.getLabel());
        activeButton = new Button(FilterType.ACTIVE.getLabel());
        completedButton = new Button(FilterType.COMPLETED.getLabel());
        
        // Set initial button states
        allButton.setTheme(ButtonTheme.PRIMARY);
        activeButton.setTheme(ButtonTheme.DEFAULT);
        completedButton.setTheme(ButtonTheme.DEFAULT);
        
        // Add click handlers
        allButton.onClick(e -> selectFilter(FilterType.ALL));
        activeButton.onClick(e -> selectFilter(FilterType.ACTIVE));
        completedButton.onClick(e -> selectFilter(FilterType.COMPLETED));

        // Create filter buttons container
        filterButtons = FlexLayout.create(allButton, activeButton, completedButton)
            .horizontal()
            .build()
            .addClassName("todo-footer-filters");

        // Setup main layout
        getBoundComponent()
            .setJustifyContent(FlexJustifyContent.BETWEEN)
            .setAlignment(FlexAlignment.CENTER)
            .addClassName("todo-footer")
            .add(itemCount, filterButtons);
    }

    /**
     * Updates the item count display.
     * 
     * @param count the number of items
     */
    public void updateItemCount(int count) {
        String text = count == 1 ? "1 item" : count + " items";
        itemCount.setText(text);
    }
    
    /**
     * Sets the filter change callback.
     * 
     * @param onFilterChange the callback to invoke when filter changes
     */
    public void setOnFilterChange(Consumer<FilterType> onFilterChange) {
        this.onFilterChange = onFilterChange;
    }
    
    /**
     * Handles filter selection.
     * 
     * @param filter the selected filter type
     */
    private void selectFilter(FilterType filter) {
        if (filter == currentFilter) return;
        
        currentFilter = filter;
        
        // Update button themes
        allButton.setTheme(filter == FilterType.ALL ? ButtonTheme.PRIMARY : ButtonTheme.DEFAULT);
        activeButton.setTheme(filter == FilterType.ACTIVE ? ButtonTheme.PRIMARY : ButtonTheme.DEFAULT);
        completedButton.setTheme(filter == FilterType.COMPLETED ? ButtonTheme.PRIMARY : ButtonTheme.DEFAULT);
        
        // Notify listener
        if (onFilterChange != null) {
            onFilterChange.accept(filter);
        }
    }
}