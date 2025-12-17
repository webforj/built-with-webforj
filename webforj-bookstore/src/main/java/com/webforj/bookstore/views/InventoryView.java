package com.webforj.bookstore.views;

import com.webforj.bookstore.domain.Book;
import com.webforj.bookstore.components.BookFormDrawer;
import com.webforj.bookstore.components.GenreManagementDrawer;
import com.webforj.bookstore.service.BookService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.icons.FeatherIcon;

import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Table;
import com.webforj.data.repository.spring.SpringDataRepository;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.App;
import com.webforj.annotation.StyleSheet;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;

/**
 * The main inventory view for managing books.
 * <p>
 * Displays a table of books with options to search, add, and edit entries.
 * Relies on {@link BookFormDrawer} for creating and updating books.
 * </p>
 * 
 * @author webforJ Bookstore
 */
@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Inventory")
@StyleSheet("ws://inventory.css")
public class InventoryView extends Composite<FlexLayout> {

  private final BookService bookService;

  private SpringDataRepository<Book, String> repository;

  private FlexLayout self = getBoundComponent();
  private FlexLayout toolbar;
  private FlexLayout tableContainer;

  private Button addButton;
  private Button manageGenresButton;
  private TextField searchField;
  private Table<Book> bookTable;
  private BookFormDrawer bookDrawer;
  private GenreManagementDrawer genreDrawer;

  /**
   * Constructs the InventoryView and initializes its components and data.
   * 
   * @param bookService the service to manage books
   * @param bookDrawer  the book form drawer component
   * @param genreDrawer the genre management drawer component
   */

  public InventoryView(BookService bookService, BookFormDrawer bookDrawer, GenreManagementDrawer genreDrawer) {
    this.bookService = bookService;
    this.bookDrawer = bookDrawer;
    this.genreDrawer = genreDrawer;

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

    manageGenresButton = new Button("Genres")
        .setTheme(ButtonTheme.DEFAULT);

    searchField = new TextField()
        .setPlaceholder("Search books...")
        .setPrefixComponent(FeatherIcon.SEARCH.create());

    bookTable = new Table<>();
    setupTableColumns();

    bookDrawer.setOnSave(book -> {
      bookService.saveBook(book);
      repository.commit();
    });
  }

  /**
   * Configures the layout of the view, arranging the toolbar and table container.
   */
  private void setupLayout() {
    self.setDirection(FlexDirection.COLUMN);
    self.addClassName("main-container");
    self.setHeight("100%");

    toolbar = new FlexLayout();
    toolbar.setDirection(FlexDirection.ROW);
    toolbar.setWrap(FlexWrap.WRAP);
    toolbar.addClassName("toolbar");
    toolbar.add(addButton, manageGenresButton, searchField);

    tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);
    tableContainer.addClassName("table-container");
    tableContainer.add(bookTable);

    self.add(toolbar, tableContainer, bookDrawer, genreDrawer);
  }

  /**
   * Sets up the columns for the book table.
   */
  private void setupTableColumns() {
    bookTable.addColumn("Title", Book::getTitle).setMinWidth(100.0f);
    bookTable.addColumn("Author", Book::getAuthor).setMinWidth(100.0f);
    bookTable.addColumn("Publisher", Book::getPublisher).setMinWidth(100.0f);
    bookTable.addColumn("Published", Book::getPublicationDate).setMinWidth(100.0f);
    bookTable.addColumn("Genres", book -> String.join(", ", book.getGenres())).setMinWidth(150.0f);
    bookTable.addColumn("ISBN", Book::getIsbn).setMinWidth(100.0f);

    bookTable.addClassName("books-table");
    bookTable.setRowHeight(45);
    bookTable.setSelectionMode(Table.SelectionMode.SINGLE);
  }

  /**
   * configures event handlers for user interactions such as button clicks and
   * search input.
   */
  private void setupEventHandlers() {
    addButton.addClickListener(e -> bookDrawer.open(null));
    manageGenresButton.addClickListener(e -> genreDrawer.open());

    bookTable.addItemClickListener(e -> bookDrawer.open(e.getItem()));

    searchField.onModify(e -> {
      String searchTerm = searchField.getValue();

      if (repository == null) {
        return;
      }

      if (searchTerm == null || searchTerm.trim().isEmpty()) {
        repository.setFilter((Specification<Book>) null);
      } else {
        String term = searchTerm.trim().toLowerCase();

        Specification<Book> searchSpec = (root, query, cb) -> {
          query.distinct(true);
          return cb.or(
              cb.like(cb.lower(root.get("title")), "%" + term + "%"),
              cb.like(cb.lower(root.get("author")), "%" + term + "%"),
              cb.like(cb.lower(root.get("publisher")), "%" + term + "%"),
              cb.like(cb.lower(root.join("genres", JoinType.LEFT)), "%" + term + "%"));
        };
        repository.setFilter(searchSpec);
      }
      repository.commit();
    });
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
