package com.webforj.bookstore.views;

import com.webforj.bookstore.domain.Book;

import com.webforj.bookstore.components.InventoryDrawer;
import com.webforj.bookstore.components.GenreChipRenderer;
import com.webforj.bookstore.service.BookService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;

import com.webforj.component.icons.TablerIcon;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.loading.Loading;
import com.webforj.component.html.elements.Div;

import com.webforj.bookstore.service.GenreService;
import com.webforj.bookstore.components.GenreDrawer;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListItem;
import com.webforj.component.table.Table;
import com.webforj.data.repository.spring.SpringDataRepository;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.App;
import com.webforj.annotation.StyleSheet;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The main inventory view for managing books.
 * <p>
 * Displays a table of books with options to search, add, and edit entries.
 * Uses {@link InventoryDrawer} for creating/updating books and managing genres.
 * </p>
 * 
 */
@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Inventory")
@StyleSheet("ws://inventory.css")
public class InventoryView extends Composite<FlexLayout> {

  @Autowired
  private BookService bookService;
  @Autowired
  private GenreService genreService;
  @Autowired
  private com.webforj.bookstore.service.AuthorService authorService;
  @Autowired
  private com.webforj.bookstore.service.PublisherService publisherService;

  private SpringDataRepository<Book, String> repository;

  private FlexLayout self = getBoundComponent();
  private Toolbar toolbar;
  private FlexLayout tableContainer;

  private Button addButton;

  private TextField searchField;
  private ChoiceBox genreFilter;
  private Table<Book> bookTable;
  private Div noResultsMessage;
  private InventoryDrawer inventoryDrawer;
  private GenreDrawer genreDrawer;
  private Loading loading = new Loading("Loading genres...");

  /**
   * Constructs the InventoryView.
   */
  public InventoryView() {
    // Default constructor
  }

  /**
   * Initializes the view after dependency injection.
   */
  @PostConstruct
  public void init() {
    initializeComponents();
    setupLayout();
    setupEventHandlers();
    loadData();

  }

  /**
   * Initializes the UI components including the add button, search field, table,
   * and drawer.
   */
  private void initializeComponents() {
    addButton = new Button("Add New")
        .setTheme(ButtonTheme.PRIMARY)
        .setPrefixComponent(FeatherIcon.PLUS.create());

    searchField = new TextField()
        .setPlaceholder("Search books...");
    searchField.setPrefixComponent(TablerIcon.create("search"));
    searchField.setStyle("width", "200px");

    genreFilter = new ChoiceBox();

    genreFilter.setWidth("max-content");
    genreFilter.setMinWidth("150px");
    genreFilter.add(new ListItem("All Genres", "All Genres"));
    genreService.getAllGenresSorted().forEach(genre -> genreFilter.add(new ListItem(genre.getName(), genre.getName())));
    genreFilter.selectIndex(0);

    bookTable = new Table<>();
    setupTableColumns();

    // Create empty state component with icon and text
    noResultsMessage = new Div();
    noResultsMessage.addClassName("empty-state");

    FlexLayout emptyStateContent = new FlexLayout();
    emptyStateContent.setDirection(FlexDirection.COLUMN);
    emptyStateContent.addClassName("empty-state-content");

    Div iconDiv = new Div();
    iconDiv.addClassName("empty-state-icon");
    iconDiv.add(FeatherIcon.BOOK.create());

    Div textDiv = new Div("That search seems to be empty.");
    textDiv.addClassName("empty-state-text");

    emptyStateContent.add(iconDiv, textDiv);
    noResultsMessage.add(emptyStateContent);
    noResultsMessage.setVisible(false);

    inventoryDrawer = new InventoryDrawer(authorService, publisherService, genreService);
    inventoryDrawer.setOnSave(book -> {
      bookService.saveBook(book);
      repository.commit();
    });

    genreDrawer = new GenreDrawer(genreService);
  }

  /**
   * Configures the layout of the view, arranging the toolbar and table container.
   */
  private void setupLayout() {
    self.setDirection(FlexDirection.COLUMN);
    self.addClassName("main-container");
    self.setHeight("100%");

    toolbar = new Toolbar();
    toolbar.addClassName("toolbar");

    Button manageGenresButton = new Button(FeatherIcon.FOLDER.create());
    manageGenresButton.addClickListener(e -> genreDrawer.open());
    manageGenresButton.setTooltipText("Manage Genres");
    manageGenresButton.setTheme(ButtonTheme.OUTLINED_GRAY);

    toolbar.addToStart(addButton, manageGenresButton);
    toolbar.addToEnd(searchField, genreFilter);

    tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);
    tableContainer.addClassName("table-container");
    tableContainer.add(bookTable, noResultsMessage);

    self.add(toolbar, tableContainer, loading);
    self.whenAttached().thenAccept(e -> getWindow().add(inventoryDrawer, genreDrawer));
  }

  /**
   * Sets up the columns for the book table.
   */
  private void setupTableColumns() {
    bookTable.addColumn("Title", Book::getTitle).setMinWidth(100.0f).setSortable(true);
    bookTable.addColumn("Author", Book::getAuthor).setMinWidth(100.0f).setSortable(true);
    bookTable.addColumn("Publisher", Book::getPublisher).setMinWidth(100.0f).setSortable(true);
    bookTable
        .addColumn("Published",
            book -> book.getPublicationDate() != null
                ? book.getPublicationDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                : "")
        .setPropertyName("publicationDate")
        .setMinWidth(100.0f)
        .setSortable(true);

    // Add ISBN column
    bookTable.addColumn("ISBN", book -> {
      String val = book.getIsbn();
      if (val != null && val.length() > 3) {
        return val.substring(0, 3) + "-" + val.substring(3);
      }
      return val;
    }).setMinWidth(110.0f).setSortable(false);

    bookTable.addColumn("Genres", book -> new GenreChipRenderer(genreService).render(book.getGenres()))
        .setMinWidth(200.0f).setSortable(false);

    bookTable.setCellPartProvider((book, column) -> {
      if ("ISBN".equals(column.getLabel())) {
        return List.of("isbn-cell");
      }
      return List.of();
    });

    bookTable.setMultiSorting(true);
    bookTable.addClassName("books-table");
    bookTable.setRowHeight(45);
    bookTable.setSelectionMode(Table.SelectionMode.SINGLE);
  }

  /**
   * configures event handlers for user interactions such as button clicks and
   * search input.
   */
  private void setupEventHandlers() {
    addButton.addClickListener(e -> inventoryDrawer.open(null));
    bookTable.addItemClickListener(e -> inventoryDrawer.open(e.getItem()));

    searchField.onModify(e -> updateFilter());
    genreFilter.onSelect(e -> updateFilter());
  }

  private void updateFilter() {
    if (repository == null) {
      return;
    }

    String searchTerm = searchField.getValue();
    String selectedGenre = null;
    if (genreFilter.getSelectedItem() != null && !genreFilter.getSelectedItem().getText().equals("All Genres")) {
      selectedGenre = genreFilter.getSelectedItem().getText();
    }

    if ((searchTerm == null || searchTerm.trim().isEmpty()) && selectedGenre == null) {
      repository.setFilter((Specification<Book>) null);
    } else {
      String term = (searchTerm != null) ? searchTerm.trim().toLowerCase() : "";
      String genre = selectedGenre;

      Specification<Book> searchSpec = (root, query, cb) -> {
        query.distinct(true);
        Predicate predicate = cb.conjunction();

        if (!term.isEmpty()) {
          predicate = cb.and(predicate, cb.or(
              cb.like(cb.lower(root.get("title")), "%" + term + "%"),
              cb.like(cb.lower(root.get("author")), "%" + term + "%")));
        }

        if (genre != null) {
          predicate = cb.and(predicate, cb.isMember(genre, root.get("genres")));
        }

        return predicate;
      };
      repository.setFilter(searchSpec);
    }
    repository.commit();

    boolean hasResults = repository.size() > 0;
    bookTable.setVisible(hasResults);
    noResultsMessage.setVisible(!hasResults);
  }

  /**
   * Loads data into the table from the book service.
   */
  private void loadData() {
    repository = bookService.getFilterableRepository();
    bookTable.setRepository(repository);
    App.console().log(repository.size());
  }

}
