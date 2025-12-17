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

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@StyleSheet("ws://drawer.css")
public class GenreManagementDrawer extends BaseDrawer {

    private final GenreService genreService;
    private FlexLayout listContainer;
    private TextField newGenreField;
    private TextField searchField;

    public GenreManagementDrawer(GenreService genreService) {
        super();
        this.genreService = genreService;
        this.setName("Manage Genres");
        getBoundComponent().setLabel("Manage Genres");
        setContent(createLayout());
        // self.close() handled by BaseDrawer
    }

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

    @Override
    public void open() {
        if (searchField != null) {
            searchField.setText("");
        }
        refreshList();
        super.open();
    }

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

    private void deleteGenre(Genre genre) {
        genreService.deleteGenre(genre.getId());
        refreshList();
    }
}
