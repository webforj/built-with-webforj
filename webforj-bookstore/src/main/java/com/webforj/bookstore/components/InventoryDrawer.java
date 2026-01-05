package com.webforj.bookstore.components;

import com.webforj.annotation.StyleSheet;
import com.webforj.bookstore.domain.Book;
import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.service.AuthorService;
import com.webforj.bookstore.service.GenreService;
import com.webforj.bookstore.service.PublisherService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.drawer.Drawer;
import com.webforj.component.drawer.Drawer.Placement;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListBox;
import com.webforj.component.list.ListItem;
import com.webforj.data.binding.annotation.BindingExclude;
import com.webforj.data.binding.annotation.UseProperty;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A unified drawer component for managing both books and genres.
 * <p>
 * This drawer can dynamically switch between two modes:
 * <ul>
 * <li>Book Form Mode - For adding or editing books</li>
 * <li>Genre Management Mode - For managing genres</li>
 * </ul>
 * </p>
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@StyleSheet("ws://drawer.css")
public class InventoryDrawer extends Composite<Drawer> {

    private enum DrawerMode {
        BOOK_FORM,
        GENRE_MANAGEMENT
    }

    private DrawerMode currentMode;
    private Drawer self = getBoundComponent();

    // Services
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenreService genreService;

    // Book Form Components
    private BindingContext<Book> bindingContext;
    private Book currentBook = new Book();
    private Consumer<Book> onSave;

    // These components will be recreated each time the form is opened
    private TextField title;

    @BindingExclude
    private ChoiceBox authorBox;

    @BindingExclude
    private ChoiceBox publisherBox;

    @UseProperty("publicationDate")
    private TextField published;

    private TextField isbn;

    @BindingExclude
    private ListBox genresBox;

    @BindingExclude
    private FlexLayout genreListContainer;

    @BindingExclude
    private TextField newGenreField;

    @BindingExclude
    private TextField genreSearchField;

    @BindingExclude
    private Button addGenreButton;

    // Shared close button
    @BindingExclude
    private Button closeButton;

    /**
     * Constructs an InventoryDrawer with the required services.
     *
     * @param authorService    the service for managing authors
     * @param publisherService the service for managing publishers
     * @param genreService     the service for managing genres
     */
    public InventoryDrawer(AuthorService authorService, PublisherService publisherService, GenreService genreService) {
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.genreService = genreService;

        configureDrawer();
        self.close();
    }

    /**
     * Configures the drawer's base settings.
     */
    private void configureDrawer() {
        self.setPlacement(Placement.BOTTOM_CENTER);
        self.addClassName("bookstore-drawer");
    }

    /**
     * Sets the callback to execute when a book is saved.
     *
     * @param onSave the callback to execute
     */
    public void setOnSave(Consumer<Book> onSave) {
        this.onSave = onSave;
    }

    /**
     * Opens the drawer in book form mode.
     *
     * @param book the book to edit, or null to create a new book
     */
    public void openBookForm(Book book) {
        currentMode = DrawerMode.BOOK_FORM;
        this.currentBook = book != null ? book : new Book();

        if (book == null) {
            self.setLabel("Add New Book");
            this.currentBook.setGenres(List.of());
        } else {
            self.setLabel("Edit Book");
        }

        self.removeAll();
        self.add(createBookFormLayout());

        populateDropdowns();
        bindingContext.read(this.currentBook);

        if (this.currentBook.getAuthor() != null) {
            selectChoiceBoxItem(authorBox, this.currentBook.getAuthor());
        } else {
            authorBox.setValue(null);
        }

        if (this.currentBook.getPublisher() != null) {
            selectChoiceBoxItem(publisherBox, this.currentBook.getPublisher());
        } else {
            publisherBox.setValue(null);
        }

        genresBox.deselectAll();
        if (this.currentBook.getGenres() != null) {
            for (String genreName : this.currentBook.getGenres()) {
                selectListBoxItem(genresBox, genreName);
            }
        }

        self.open();
    }

    /**
     * Opens the drawer in genre management mode.
     */
    public void openGenreManagement() {
        currentMode = DrawerMode.GENRE_MANAGEMENT;
        self.setLabel("Manage Genres");

        self.removeAll();
        self.add(createGenreManagementLayout());

        if (genreSearchField != null) {
            genreSearchField.setText("");
        }
        refreshGenreList();

        self.open();
    }

    /**
     * Creates the book form layout.
     *
     * @return the configured FlexLayout for book form
     */
    private FlexLayout createBookFormLayout() {
        title = new TextField("Title");
        authorBox = new ChoiceBox("Author");
        publisherBox = new ChoiceBox("Publisher");
        published = new TextField("Published (e.g. October 6, 2016)");
        isbn = new TextField("ISBN");
        genresBox = new ListBox("Genres");
        genresBox.setStyle("height", "150px");

        FlexLayout formLayout = new FlexLayout();
        formLayout.setDirection(FlexDirection.COLUMN);
        formLayout.addClassName("drawer-content");

        formLayout.add(title, authorBox, publisherBox, published, isbn, genresBox);

        FlexLayout buttonLayout = new FlexLayout();
        buttonLayout.addClassName("drawer-button-bar");

        Button saveButton = new Button("Save").setTheme(ButtonTheme.PRIMARY);
        Button cancelButton = new Button("Cancel");

        saveButton.onClick(e -> saveBook());
        cancelButton.onClick(e -> handleClose());

        buttonLayout.add(saveButton, cancelButton);
        formLayout.add(buttonLayout);

        // Initialize binding context with fresh components
        bindingContext = BindingContext.of(this, Book.class, true);

        return formLayout;
    }

    /**
     * Creates the genre management layout.
     *
     * @return the configured FlexLayout for genre management
     */
    private FlexLayout createGenreManagementLayout() {
        FlexLayout mainLayout = new FlexLayout();
        mainLayout.setDirection(FlexDirection.COLUMN);
        mainLayout.addClassName("genre-main-layout");

        // Add New Genre Section
        FlexLayout addSection = new FlexLayout();
        addSection.addClassName("genre-add-section");

        genreSearchField = new TextField();
        genreSearchField.setPlaceholder("Search");
        genreSearchField.setStyle("flex-grow", "1");
        genreSearchField.onModify(e -> refreshGenreList());

        newGenreField = new TextField();
        newGenreField.setPlaceholder("New Genre Name");
        newGenreField.setStyle("flex-grow", "1");

        addGenreButton = new Button("Add");
        addGenreButton.setTheme(ButtonTheme.PRIMARY);
        addGenreButton.onClick(e -> addGenre());

        addSection.add(genreSearchField, newGenreField, addGenreButton);
        mainLayout.add(addSection);

        // List of Genres
        genreListContainer = new FlexLayout();
        genreListContainer.setDirection(FlexDirection.COLUMN);
        genreListContainer.addClassName("genre-list-container");
        mainLayout.add(genreListContainer);

        // Close button at the bottom
        Button closeButton = new Button("Close");
        closeButton.onClick(e -> handleClose());
        mainLayout.add(closeButton);

        return mainLayout;
    }

    /**
     * Handles the close action based on current mode.
     */
    private void handleClose() {
        if (currentMode == DrawerMode.GENRE_MANAGEMENT && genreSearchField != null) {
            genreSearchField.setText("");
        }
        self.close();
    }

    /**
     * Populates the author, publisher, and genre dropdowns.
     */
    private void populateDropdowns() {
        authorBox.removeAll();
        authorService.getAllAuthorsSorted().stream()
                .filter(a -> a.getName() != null && !a.getName().trim().isEmpty())
                .forEach(a -> authorBox.add(a.getName()));

        publisherBox.removeAll();
        publisherService.getAllPublishersSorted().stream()
                .filter(p -> p.getName() != null && !p.getName().trim().isEmpty())
                .forEach(p -> publisherBox.add(p.getName()));

        genresBox.removeAll();
        genreService.getAllGenresSorted().stream()
                .filter(g -> g.getName() != null && !g.getName().trim().isEmpty())
                .forEach(g -> genresBox.add(g.getName()));
    }

    /**
     * Refreshes the genre list based on search criteria.
     */
    private void refreshGenreList() {
        genreListContainer.removeAll();
        String searchTerm = genreSearchField != null ? genreSearchField.getText() : "";
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
            genreListContainer.add(row);
        }
    }

    /**
     * Adds a new genre.
     */
    private void addGenre() {
        String name = newGenreField.getText().trim();
        if (!name.isEmpty()) {
            Genre newGenre = new Genre();
            newGenre.setName(name);
            genreService.saveGenre(newGenre);
            newGenreField.setText("");
            refreshGenreList();
        }
    }

    /**
     * Deletes the specified genre.
     *
     * @param genre the genre to delete
     */
    private void deleteGenre(Genre genre) {
        genreService.deleteGenre(genre.getId());
        refreshGenreList();
    }

    /**
     * Saves the current book.
     */
    private void saveBook() {
        ValidationResult result = bindingContext.write(this.currentBook);
        if (result.isValid()) {
            if (authorBox.getSelectedItem() != null) {
                this.currentBook.setAuthor(authorBox.getSelectedItem().getText());
            } else {
                this.currentBook.setAuthor(null);
            }

            if (publisherBox.getSelectedItem() != null) {
                this.currentBook.setPublisher(publisherBox.getSelectedItem().getText());
            } else {
                this.currentBook.setPublisher(null);
            }

            List<String> selectedGenres = new ArrayList<>();
            for (ListItem item : genresBox.getSelectedItems()) {
                selectedGenres.add(item.getText());
            }
            this.currentBook.setGenres(selectedGenres);

            if (onSave != null) {
                onSave.accept(this.currentBook);
            }
            self.close();
        }
    }

    /**
     * Selects an item in a ChoiceBox by text.
     *
     * @param box  the ChoiceBox
     * @param text the text to match
     */
    private void selectChoiceBoxItem(ChoiceBox box, String text) {
        for (ListItem item : box.getItems()) {
            if (item.getText().equals(text)) {
                box.select(item);
                return;
            }
        }
        box.setValue(null);
    }

    /**
     * Selects items in a ListBox by text (supports multiple selection).
     *
     * @param box  the ListBox
     * @param text the text to match
     */
    private void selectListBoxItem(ListBox box, String text) {
        for (ListItem item : box.getItems()) {
            if (item.getText().equals(text)) {
                box.select(item);
            }
        }
    }
}
