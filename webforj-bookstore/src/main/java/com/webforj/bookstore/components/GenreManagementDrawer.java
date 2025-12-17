package com.webforj.bookstore.components;

import com.webforj.annotation.StyleSheet;
import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.service.GenreService;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A drawer component for managing genres in the bookstore application.
 * <p>
 * This component allows users to:
 * <ul>
 * <li>View a list of all existing genres.</li>
 * <li>Search for genres by name.</li>
 * <li>Add new genres.</li>
 * <li>Delete existing genres.</li>
 * </ul>
 * </p>
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@StyleSheet("ws://drawer.css")
public class GenreManagementDrawer extends BaseDrawer {

    private final GenreService genreService;
    private FlexLayout listContainer;
    private TextField newGenreField;
    private TextField searchField;

    /**
     * Constructs the GenreManagementDrawer with the specified genre service.
     *
     * @param genreService the service used for managing genre data
     */
    public GenreManagementDrawer(GenreService genreService) {
        super();
        this.genreService = genreService;
        this.setName("Manage Genres");
        getBoundComponent().setLabel("Manage Genres");
        setContent(createLayout());
        // self.close() handled by BaseDrawer
    }

    /**
     * Creates and configures the main layout for the genre management drawer.
     * <p>
     * The layout includes a search field, an input field for new genres,
     * an add button, and a scrollable list of existing genres.
     * </p>
     *
     * @return the configured FlexLayout containing all UI elements
     */
    private FlexLayout createLayout() {
        FlexLayout mainLayout = new FlexLayout();
        mainLayout.setDirection(FlexDirection.COLUMN);
        mainLayout.addClassName("genre-main-layout");

        // Add New Genre Section
        FlexLayout addSection = new FlexLayout();
        addSection.addClassName("genre-add-section");

        searchField = new TextField();
        searchField.setPlaceholder("Search");
        searchField.setStyle("flex-grow", "1");
        searchField.onModify(e -> refreshList());

        newGenreField = new TextField();
        newGenreField.setPlaceholder("New Genre Name");
        newGenreField.setStyle("flex-grow", "1");

        Button addButton = new Button("Add");
        addButton.setTheme(ButtonTheme.PRIMARY);
        addButton.addClickListener(e -> addGenre());

        addSection.add(searchField, newGenreField, addButton);
        mainLayout.add(addSection);

        // List of Genres
        listContainer = new FlexLayout();
        listContainer.setDirection(FlexDirection.COLUMN);
        listContainer.addClassName("genre-list-container");

        mainLayout.add(listContainer);

        // Close button at the bottom
        Button closeButton = new Button("Close", e -> close());
        mainLayout.add(closeButton);

        return mainLayout;
    }

    /**
     * Opens the drawer and refreshes the genre list.
     * <p>
     * Clears the search field and ensures the list displays the most up-to-date
     * data from the service before showing the drawer to the user.
     * </p>
     */
    @Override
    public void open() {
        if (searchField != null) {
            searchField.setText("");
        }
        refreshList();
        super.open();
    }

    /**
     * Refreshes the list of genres displayed in the drawer.
     * <p>
     * If a search term is present in the search field, the list is filtered
     * to show only matching genres. Otherwise, all genres are displayed
     * sorted alphabetically.
     * </p>
     */
    private void refreshList() {
        listContainer.removeAll();
        String searchTerm = searchField != null ? searchField.getText() : "";
        List<Genre> genres;

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            genres = genreService.getAllGenresSorted();
        } else {
            genres = genreService.searchGenres(searchTerm.trim());
        }

        for (Genre genre : genres) {
            FlexLayout row = new FlexLayout();
            row.setDirection(FlexDirection.ROW);
            row.addClassName("genre-list-row");

            Div nameLabel = new Div(genre.getName());
            nameLabel.addClassName("genre-name-label");

            Button deleteBtn = new Button(FeatherIcon.TRASH.create());
            deleteBtn.setTheme(ButtonTheme.DANGER);
            deleteBtn.addClickListener(e -> deleteGenre(genre));

            row.add(nameLabel, deleteBtn);
            listContainer.add(row);
        }
    }

    /**
     * Adds a new genre using the name entered in the new genre text field.
     * <p>
     * The method trims whitespace from the input. If the name is valid (not empty),
     * a new genre is created and saved via the service. The field is then cleared
     * and the list is refreshed.
     * </p>
     */
    private void addGenre() {
        String name = newGenreField.getText().trim();
        if (!name.isEmpty()) {
            Genre newGenre = new Genre();
            newGenre.setName(name);
            genreService.saveGenre(newGenre);
            newGenreField.setText("");
            refreshList();
        }
    }

    /**
     * Deletes the specified genre.
     * <p>
     * Removes the genre via the service and refreshes the displayed list.
     * </p>
     *
     * @param genre the genre to delete
     */
    private void deleteGenre(Genre genre) {
        genreService.deleteGenre(genre.getId());
        refreshList();
    }
}
