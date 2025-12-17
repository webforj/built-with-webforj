package com.webforj.bookstore.components;

import com.webforj.bookstore.domain.Book;
import com.webforj.bookstore.service.AuthorService;
import com.webforj.bookstore.service.GenreService;
import com.webforj.bookstore.service.PublisherService;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListBox;
import com.webforj.component.list.ListItem;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A drawer component for creating and editing books.
 * <p>
 * This component slides up from the bottom of the screen and provides a form
 * with fields for title, author, publisher, publication date, ISBN, and genres.
 * </p>
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BookFormDrawer extends BaseDrawer {

    // Removed 'self' as it is managed by BaseDrawer if we only use it for config.
    // However, if we need 'self' to access specific Drawer methods not exposed by
    // BaseDrawer, we can keep it but BaseDrawer handles the bound component.
    // Composite.getBoundComponent() always returns the same instance.

    private BindingContext<Book> bindingContext;
    private Book currentBook = new Book();
    private Consumer<Book> onSave;

    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenreService genreService;

    private TextField title = new TextField("Title");
    private ChoiceBox authorBox = new ChoiceBox("Author");
    private ChoiceBox publisherBox = new ChoiceBox("Publisher");
    private TextField publicationDate = new TextField("Published (e.g. October 6, 2016)");
    private TextField isbn = new TextField("ISBN");
    private ListBox genresBox = new ListBox("Genres");

    private Button saveButton = new Button("Save").setTheme(ButtonTheme.PRIMARY);
    private Button cancelButton = new Button("Cancel");

    /**
     * Constructs a new BookFormDrawer.
     * 
     * @param authorService    the service to retrieve authors
     * @param publisherService the service to retrieve publishers
     * @param genreService     the service to retrieve genres
     */
    public BookFormDrawer(AuthorService authorService, PublisherService publisherService, GenreService genreService) {
        super();
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.genreService = genreService;
        configureComponents();
        setContent(createLayout());
        bindFields();
        setupEventHandlers();
    }

    /**
     * Sets the callback to execute when a book is saved.
     * 
     * @param onSave the callback to execute
     */
    public void setOnSave(Consumer<Book> onSave) {
        this.onSave = onSave;
    }

    private void configureComponents() {
        // Explicitly set height for ListBox in a drawer to avoid it being too tall or
        // too squashed
        // Depending on CSS, but setting a reasonable default
        genresBox.setStyle("height", "150px");
    }

    private FlexLayout createLayout() {
        FlexLayout formLayout = new FlexLayout();
        formLayout.setDirection(FlexDirection.COLUMN);
        formLayout.setStyle("gap", "15px");
        formLayout.setPadding("20px");

        formLayout.add(title, authorBox, publisherBox, publicationDate, isbn, genresBox);

        FlexLayout buttonLayout = new FlexLayout();
        buttonLayout.setStyle("gap", "10px");
        buttonLayout.add(saveButton, cancelButton);

        formLayout.add(buttonLayout);
        return formLayout;
    }

    private void bindFields() {
        // Only bind fields that map directly and aren't handled manually
        // bindingContext = BindingContext.of(this, Book.class, true);
    }

    private void setupEventHandlers() {
        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> close());
    }

    /**
     * Opens the drawer for adding a new book or editing an existing one.
     * 
     * @param book the book to edit, or null to create a new book
     */
    public void open(Book book) {
        this.currentBook = book != null ? book : new Book();
        if (book == null) {
            getBoundComponent().setLabel("Add New Book");
            this.currentBook.setGenres(List.of());
        } else {
            getBoundComponent().setLabel("Edit Book");
        }

        // Populate Data
        populateDropdowns();

        // Bind basic fields
        bindingContext.read(this.currentBook);

        // Manual bind for ChoiceBox/ListBox
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

        // Clear previous selection
        genresBox.deselectAll();
        if (this.currentBook.getGenres() != null) {
            for (String genreName : this.currentBook.getGenres()) {
                selectListBoxItem(genresBox, genreName);
            }
        }

        super.open();
    }

    private void populateDropdowns() {
        // Clear and Repopulate
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

    private void selectChoiceBoxItem(ChoiceBox box, String text) {
        // WebforJ helper to select by text if possible, or iterate
        for (ListItem item : box.getItems()) {
            if (item.getText().equals(text)) {
                box.select(item);
                return;
            }
        }
        box.setValue(null);
        // If we supported "New Author" entry, we would handle custom text here
        // differently
    }

    private void selectListBoxItem(ListBox box, String text) {
        for (ListItem item : box.getItems()) {
            if (item.getText().equals(text)) {
                box.select(item);
                // Don't return, as multiple selection is possible
            }
        }
    }

    private void save() {
        ValidationResult result = bindingContext.write(this.currentBook);
        if (result.isValid()) {
            // Manual read from components
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
            close();
        }
    }
}
