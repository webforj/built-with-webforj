package com.webforj.bookstore.views;

import com.webforj.bookstore.domain.Book;

import com.webforj.bookstore.components.InventoryDrawer;
import com.webforj.bookstore.service.BookService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;

import com.webforj.component.icons.TablerIcon;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.html.elements.Div;

import com.webforj.bookstore.service.AuthorService;
import com.webforj.bookstore.service.GenreService;
import com.webforj.bookstore.service.PublisherService;
import com.webforj.bookstore.components.GenreDrawer;
import com.webforj.bookstore.components.GenreChipRenderer;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListItem;
import com.webforj.component.table.Table;
import com.webforj.data.repository.spring.SpringDataRepository;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.google.gson.Gson;
import com.webforj.annotation.StyleSheet;
import org.springframework.data.jpa.domain.Specification;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The main inventory view for managing books.
 * <p>
 * Displays a table of books with options to search, add, and edit entries.
 * Uses {@link InventoryDrawer} for creating/updating books and managing genres.
 * </p>
 */
@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Inventory")
@StyleSheet("ws://inventory.css")
public class InventoryView extends Composite<FlexLayout> {

  private final BookService bookService;
  private final GenreService genreService;
  private final AuthorService authorService;
  private final PublisherService publisherService;

  private SpringDataRepository<Book, String> repository;

  private static final String DEFAULT_GENRE_COLOR = "rgba(107, 107, 107, 1)";
  private static final Gson GSON = new Gson();
  private Map<String, String> genreColorCache = new HashMap<>();

  private FlexLayout self = getBoundComponent();

  private Button addButton;

  private TextField searchField;
  private ChoiceBox genreFilter;
  private Table<Book> bookTable;
  private Div noResultsMessage;
  private InventoryDrawer inventoryDrawer;
  private GenreDrawer genreDrawer;

  /**
   * Constructs the InventoryView with required services.
   *
   * @param bookService      the service for managing books
   * @param genreService     the service for managing genres
   * @param authorService    the service for managing authors
   * @param publisherService the service for managing publishers
   */
  public InventoryView(BookService bookService, GenreService genreService,
      AuthorService authorService,
      PublisherService publisherService) {
    this.bookService = bookService;
    this.genreService = genreService;
    this.authorService = authorService;
    this.publisherService = publisherService;
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
    searchField.addClassName("search-field");

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
    genreDrawer.setOnClose(() -> {
      refreshGenreCache();
      String selected = genreFilter.getSelectedItem() != null
          ? genreFilter.getSelectedItem().getText()
          : "All Genres";
      genreFilter.removeAll();
      genreFilter.add(new ListItem("All Genres", "All Genres"));
      genreService.getAllGenresSorted()
          .forEach(genre -> genreFilter.add(new ListItem(genre.getName(), genre.getName())));
      genreFilter.selectKey(selected);
      if (repository != null) {
        repository.commit();
      }
    });
  }

  /**
   * Configures the layout of the view, arranging the toolbar and table container.
   */
  private void setupLayout() {
    self.setDirection(FlexDirection.COLUMN);
    self.addClassName("main-container");
    self.setHeight("100%");
    self.setSpacing("0px");

    Toolbar toolbar = new Toolbar();
    toolbar.addClassName("toolbar");

    Button manageGenresButton = new Button("Genres");
    manageGenresButton.setPrefixComponent(TablerIcon.create("tags"));
    manageGenresButton.addClickListener(e -> genreDrawer.open());
    manageGenresButton.setTooltipText("Manage Genres");

    toolbar.addToStart(addButton);
    toolbar.addToEnd(searchField, genreFilter, manageGenresButton);

    FlexLayout tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);
    tableContainer.addClassName("table-container");
    tableContainer.add(bookTable, noResultsMessage);

    self.add(toolbar, tableContainer);
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

    bookTable.addColumn("Genres", book -> GSON.toJson(book.getGenres() == null ? List.of()
        : book.getGenres().stream()
            .map(name -> Map.of("name", name,
                "color", genreColorCache.getOrDefault(name, DEFAULT_GENRE_COLOR)))
            .collect(Collectors.toList())))
        .setMinWidth(200.0f).setSortable(false)
        .setRenderer(new GenreChipRenderer<>());

    bookTable.setCellPartProvider((book, column) -> {
      if ("ISBN".equals(column.getLabel())) {
        return List.of("isbn-cell");
      }
      return List.of();
    });

    bookTable.setMultiSorting(true);
    bookTable.addClassName("books-table");
    bookTable.setHeaderHeight(52);
    bookTable.setRowHeight(52);
    bookTable.setSelectionMode(Table.SelectionMode.SINGLE);
  }

  /**
   * Configures event handlers for user interactions such as button clicks and
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
          String escaped = escapeLikePattern(term);
          predicate = cb.and(predicate, cb.or(
              cb.like(cb.lower(root.get("title")), "%" + escaped + "%", '\\'),
              cb.like(cb.lower(root.get("author")), "%" + escaped + "%", '\\')));
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
   * Loads data into the table from the book service and refreshes the genre color
   * cache.
   */
  private void loadData() {
    refreshGenreCache();
    repository = bookService.getFilterableRepository();
    bookTable.setRepository(repository);
  }

  /**
   * Refreshes the genre color cache and the genre filter dropdown from the
   * current DB state.
   */
  private void refreshGenreCache() {
    genreColorCache.clear();
    genreService.getAllGenresSorted().forEach(g -> genreColorCache.put(g.getName(),
        g.getColor() != null && !g.getColor().isEmpty() ? g.getColor() : DEFAULT_GENRE_COLOR));
  }

  /**
   * Escapes SQL LIKE wildcards in user-supplied search input.
   */
  private static String escapeLikePattern(String input) {
    return input.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
  }
}
