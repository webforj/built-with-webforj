package com.webforj.bookstore.components;

import com.webforj.annotation.StyleSheet;
import com.webforj.bookstore.domain.Book;

import com.webforj.bookstore.service.AuthorService;
import com.webforj.bookstore.service.GenreService;
import com.webforj.bookstore.service.PublisherService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.drawer.Drawer;
import com.webforj.component.drawer.Drawer.Placement;
import com.webforj.component.field.MaskedDateField;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H2;
import com.webforj.component.icons.FeatherIcon;

import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListBox;
import com.webforj.component.list.MultipleSelectableList;
import com.webforj.data.binding.annotation.BindingExclude;
import com.webforj.data.binding.annotation.UseProperty;

import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A drawer component for managing books.
 * <p>
 * <p>
 * This drawer is used for:
 * <ul>
 * <li>Adding or editing books</li>
 * </ul>
 * </p>
 */
@StyleSheet("ws://drawer.css")
public class InventoryDrawer extends Composite<Drawer> {

    private Drawer self = getBoundComponent();

    // Services
    private AuthorService authorService;
    private PublisherService publisherService;
    private GenreService genreService;

    // Book Form Components
    private BindingContext<Book> bindingContext;
    private Book currentBook = new Book();
    private Consumer<Book> onSave;

    // These components will be recreated each time the form is opened
    private TextField title;

    @UseProperty("author")
    private ChoiceBox authorBox;

    @UseProperty("publisher")
    private ChoiceBox publisherBox;

    @UseProperty("publicationDate")
    private MaskedDateField published;

    private TextField isbn;

    @UseProperty("genres")
    private ListBox genresBox;

    // Shared close button
    @BindingExclude
    private Button closeButton;

    private H2 label;

    /**
     * Constructs an InventoryDrawer.
     * 
     * @param authorService    the author service for managing authors
     * @param publisherService the publisher service for managing publishers
     * @param genreService     the genre service for managing genres
     */
    public InventoryDrawer(AuthorService authorService, PublisherService publisherService, GenreService genreService) {
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.genreService = genreService;

        self.add(createBookFormLayout());
        configureDrawer();
        self.close();
    }

    /**
     * Configures the drawer's base settings.
     */
    private void configureDrawer() {
        self.setPlacement(Placement.BOTTOM_CENTER);
        self.addClassName("bookstore-drawer");
        label = new H2("");
        // Create title layout with icon and text aligned on baseline
        FlexLayout titleLayout = new FlexLayout();
        titleLayout.setDirection(FlexDirection.ROW);
        titleLayout.setSpacing("var(--dwc-space-s)");
        titleLayout.setAlignment(FlexAlignment.BASELINE);

        titleLayout.add(FeatherIcon.BOOK.create(), label);
        self.addToTitle(titleLayout);
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
     * Opens the drawer for adding or editing a book.
     *
     * @param book the book to edit, or null to create a new book
     */
    public void open(Book book) {
        this.currentBook = book != null ? book : new Book();

        if (book == null) {
            label.setText("Add New Book");
            this.currentBook.setGenres(List.of());
        } else {
            label.setText("Edit Book");
        }

        populateDropdowns();
        bindingContext.read(this.currentBook);

        self.open();
    }

    /**
     * Creates the book form layout.
     *
     * @return the configured FlexLayout for book form
     */
    private FlexLayout createBookFormLayout() {
        title = new TextField("Title");
        title.setPrefixComponent(FeatherIcon.TYPE.create());

        authorBox = new ChoiceBox("Author");
        publisherBox = new ChoiceBox("Publisher");

        published = new MaskedDateField("Published");
        published.setAllowCustomValue(false);
        published.getPicker().setAutoOpen(true);

        isbn = new TextField("ISBN");
        isbn.setPrefixComponent(FeatherIcon.HASH.create());
        isbn.setPlaceholder("9780000000000"); // Example format
        genresBox = new ListBox("Genres");
        genresBox.addClassName("genres-box");

        genresBox.setSelectionMode(MultipleSelectableList.SelectionMode.MULTIPLE);

        FlexLayout formLayout = new FlexLayout();
        formLayout.setDirection(FlexDirection.COLUMN);
        formLayout.addClassName("drawer-content");

        formLayout.add(title, authorBox, publisherBox, published, isbn, genresBox);

        Button saveButton = new Button("Save").setTheme(ButtonTheme.PRIMARY)
                .setPrefixComponent(FeatherIcon.SAVE.create());
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("margin-right", "var(--dwc-space-s)");
        cancelButton.setStyle("margin-left", "var(--dwc-space-s)");

        saveButton.onClick(e -> saveBook());
        cancelButton.onClick(e -> self.close());

        self.addToFooter(saveButton, cancelButton);

        // Initialize binding context with fresh components
        bindingContext = BindingContext.of(this, Book.class, true);

        return formLayout;
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
     * Saves the current book.
     */
    private void saveBook() {

        ValidationResult result = bindingContext.write(this.currentBook);
        if (result.isValid()) {
            if (this.currentBook.getId() == null || this.currentBook.getId().isEmpty()) {
                this.currentBook.setId(UUID.randomUUID().toString());
            }

            if (onSave != null) {
                onSave.accept(this.currentBook);
            }
            self.close();
        }
    }

}
