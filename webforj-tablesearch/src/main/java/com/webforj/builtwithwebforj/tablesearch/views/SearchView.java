package com.webforj.builtwithwebforj.tablesearch.views;

import com.webforj.Environment;
import com.webforj.builtwithwebforj.tablesearch.search.Filters;
import com.webforj.builtwithwebforj.tablesearch.search.Product;
import com.webforj.builtwithwebforj.tablesearch.search.ProductCatalog;
import com.webforj.builtwithwebforj.tablesearch.search.ProductQuery;
import com.webforj.builtwithwebforj.tablesearch.search.QueryParser;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.card.Card;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Column;
import com.webforj.component.table.Table;
import com.webforj.component.toast.Toast;
import com.webforj.data.repository.CollectionRepository;
import com.webforj.router.annotation.Route;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Route(value = "/", outlet = MainLayout.class)
public class SearchView extends Composite<FlexLayout> {
  private static final long TIMEOUT_SECONDS = 30;

  // NumberFormat is not thread-safe, so each view keeps its own.
  private final NumberFormat price = NumberFormat.getCurrencyInstance(Locale.US);

  private final TextField ask = new TextField("Ask in plain English");
  private final Button search = new Button("Search", ButtonTheme.PRIMARY);
  private final Button clear = new Button("Show all");
  private final Paragraph understood = new Paragraph();
  private final Table<Product> table = new Table<>();
  private final CollectionRepository<Product> repository;
  private final ProductCatalog catalog;
  private final QueryParser parser;
  // Increases with every search and every reset, so a late reply can tell it is out of date.
  private int searchId;

  public SearchView(ProductCatalog catalog, QueryParser parser) {
    this.catalog = catalog;
    this.parser = parser;
    repository = new CollectionRepository<>(catalog.all());

    ask.setPlaceholder("laptops under 1000 that are in stock");
    ask.setPrefixComponent(TablerIcon.create("search"));
    ask.onKeypress(e -> {
      if (e.getKeyCode() == KeypressEvent.Key.ENTER) {
        runSearch();
      }
    });
    search.setPrefixComponent(TablerIcon.create("sparkles"));
    search.onClick(e -> runSearch());
    clear.onClick(e -> reset());

    FlexLayout bar = new FlexLayout(ask, search, clear);
    bar.addClassName("tablesearch__bar");
    bar.setWrap(FlexWrap.WRAP);
    bar.setAlignment(FlexAlignment.END);

    understood.addClassName("tablesearch__understood");
    Card searchCard = new Card(bar, understood);
    searchCard.addToIcon(TablerIcon.create("message-search"));
    searchCard.addToTitle(new Span("Ask the table"));
    searchCard.addToCaption(new Span("Categories: " + String.join(", ", catalog.categories())));

    table.setRepository(repository);
    table.addColumn("name", Product::name).setLabel("Name").setFlex(2f).setMinWidth(120f);
    table.addColumn("category", Product::category).setLabel("Category").setFlex(1f).setMinWidth(96f);
    table.addColumn("price", p -> price.format(p.price())).setLabel("Price")
        .setAlignment(Column.Alignment.RIGHT).setFlex(1f).setMinWidth(88f);
    table.addColumn("stock", Product::stock).setLabel("In stock")
        .setAlignment(Column.Alignment.RIGHT).setFlex(1f).setMinWidth(72f);
    Card tableCard = new Card(table);
    tableCard.addToIcon(TablerIcon.create("table"));
    tableCard.addToTitle(new Span("Products"));

    Div header = new Div(
        new H2("Search a table in plain English"),
        new Paragraph("Type what you want the way you would say it. The AI turns it into a "
            + "filter and the table applies it. The AI never sees the rows."));

    getBoundComponent().setDirection(FlexDirection.COLUMN).addClassName("tablesearch")
        .add(header, searchCard, tableCard);
    reset();
  }

  private void runSearch() {
    String text = ask.getValue();
    if (text == null || text.isBlank() || !search.isEnabled()) {
      return;
    }
    int id = ++searchId;
    search.setEnabled(false);
    understood.setText("Reading your search...");
    // The reply arrives on a Spring worker thread, so capture this session's Environment here.
    Environment env = Environment.getCurrent();
    parser.parse(text)
        .orTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .whenComplete((query, error) -> Environment.runLater(env, () -> {
          if (id != searchId) {
            return;
          }
          search.setEnabled(true);
          if (error != null || query == null) {
            showAll("Could not read that search. Try rephrasing it.");
            Toast.show("Could not read that search. Try rephrasing it.", Theme.DANGER);
            return;
          }
          apply(query);
        }));
  }

  private void apply(ProductQuery query) {
    if (query.category() != null && catalog.categories().stream()
        .noneMatch(c -> c.equalsIgnoreCase(query.category()))) {
      showAll("There is no category \"" + query.category() + "\". Try one of: "
          + String.join(", ", catalog.categories()) + ". Showing all "
          + count(catalog.all().size()) + ".");
      return;
    }
    repository.setBaseFilter(Filters.from(query));
    repository.commit();
    String filter = describe(query);
    String shown = count(repository.findAll().count());
    understood.setText(filter.isEmpty()
        ? "Nothing to filter on, so showing " + shown + "."
        : "Understood " + filter + ". Showing " + shown + ".");
  }

  private void reset() {
    ask.setValue("");
    search.setEnabled(true);
    showAll("Showing all " + count(catalog.all().size()) + ".");
  }

  private void showAll(String message) {
    searchId++;
    repository.setBaseFilter(p -> true);
    repository.commit();
    understood.setText(message);
  }

  private String describe(ProductQuery q) {
    List<String> parts = new ArrayList<>();
    if (q.category() != null) {
      parts.add("category " + q.category());
    }
    if (q.nameContains() != null) {
      parts.add("name contains \"" + q.nameContains() + "\"");
    }
    if (q.minPrice() != null) {
      parts.add("from " + price.format(q.minPrice()));
    }
    if (q.maxPrice() != null) {
      parts.add("up to " + price.format(q.maxPrice()));
    }
    if (q.inStock() != null) {
      parts.add(q.inStock() ? "in stock" : "out of stock");
    }
    return String.join(", ", parts);
  }

  private static String count(long n) {
    return n + (n == 1 ? " product" : " products");
  }
}
