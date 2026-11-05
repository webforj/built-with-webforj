package com.webforj.builtwithwebforj.tablesearch.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.tablesearch.search.ProductQuery;
import com.webforj.builtwithwebforj.tablesearch.search.QueryParser;
import com.webforj.component.button.Button;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.table.Table;
import com.webforj.component.toast.Toast;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = {MainLayout.class, SearchView.class})
class SearchViewTest {

  @MockitoBean
  private QueryParser parser;

  private void answer(String text, ProductQuery query) {
    when(parser.parse(text)).thenReturn(CompletableFuture.completedFuture(query));
  }

  @Test
  void shouldShowEveryProductAtFirst(WebforjTestbed context) {
    context.navigate(SearchView.class);

    assertEquals(13, rows());
    assertEquals("Showing all 13 products.", understood());
  }

  @Test
  void shouldFilterTheTableWithTheParsedQuery(WebforjTestbed context) {
    answer("laptops under 1000 in stock",
        new ProductQuery("Laptops", null, null, new BigDecimal("1000"), true));

    search(context, "laptops under 1000 in stock");
    clickSearch();

    assertEquals(1, rows());
    assertEquals("Understood category Laptops, up to $1,000.00, in stock. Showing 1 product.",
        understood());
  }

  @Test
  void shouldSearchWhenEnterIsPressed(WebforjTestbed context) {
    answer("mx", new ProductQuery(null, "mx", new BigDecimal("50"), null, null));

    search(context, "mx");
    UserInteraction.keypress(field(), KeypressEvent.Key.ENTER);

    assertEquals(2, rows());
    assertEquals("Understood name contains \"mx\", from $50.00. Showing 2 products.",
        understood());
  }

  @Test
  void shouldDescribeAnOutOfStockSearch(WebforjTestbed context) {
    answer("laptops that are out of stock",
        new ProductQuery("Laptops", null, null, null, false));

    search(context, "laptops that are out of stock");
    clickSearch();

    assertEquals(1, rows());
    assertEquals("Understood category Laptops, out of stock. Showing 1 product.", understood());
  }

  @Test
  void shouldShowAllRowsForAnUnknownCategory(WebforjTestbed context) {
    answer("mice", new ProductQuery("Mice", null, null, null, null));
    answer("notebooks", new ProductQuery("Notebooks", null, null, null, null));
    search(context, "mice");
    clickSearch();
    assertEquals(2, rows());

    UserInteraction.type(field(), "notebooks");
    clickSearch();

    assertEquals(13, rows());
    assertEquals("There is no category \"Notebooks\". Try one of: Laptops, Tablets, Keyboards, "
        + "Monitors, Mice. Showing all 13 products.", understood());
  }

  @Test
  void shouldSayWhenNothingWasRecognized(WebforjTestbed context) {
    answer("hello", new ProductQuery(null, null, null, null, null));

    search(context, "hello");
    clickSearch();

    assertEquals("Nothing to filter on, so showing 13 products.", understood());
  }

  @Test
  void shouldReportAFailedSearch(WebforjTestbed context) {
    when(parser.parse(anyString()))
        .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("quota")));

    search(context, "laptops");
    clickSearch();

    assertEquals("Could not read that search. Try rephrasing it.", understood());
    assertEquals("Could not read that search. Try rephrasing it.",
        ComponentLocator.of(Toast.class).locate().getText());
    assertEquals(13, rows());
  }

  @Test
  void shouldIgnoreAReplyThatArrivesAfterShowAll(WebforjTestbed context) {
    CompletableFuture<ProductQuery> reply = new CompletableFuture<>();
    when(parser.parse("mice")).thenReturn(reply);
    search(context, "mice");
    clickSearch();

    UserInteraction.click(ComponentLocator.of(Button.class).byText("Show all").locate());
    reply.complete(new ProductQuery("Mice", null, null, null, null));

    assertEquals(13, rows());
    assertEquals("Showing all 13 products.", understood());
  }

  @Test
  void shouldDisableSearchWhileTheModelIsReading(WebforjTestbed context) {
    when(parser.parse("mice")).thenReturn(new CompletableFuture<>());

    search(context, "mice");
    clickSearch();

    assertFalse(searchButton().isEnabled());
    assertEquals("Reading your search...", understood());
  }

  @Test
  void shouldIgnoreAnEmptySearch(WebforjTestbed context) {
    search(context, "   ");
    clickSearch();

    assertTrue(searchButton().isEnabled());
    assertEquals("Showing all 13 products.", understood());
    verify(parser, never()).parse(anyString());
  }

  private static void search(WebforjTestbed context, String text) {
    context.navigate(SearchView.class);
    UserInteraction.type(field(), text);
  }

  private static void clickSearch() {
    UserInteraction.click(searchButton());
  }

  private static Button searchButton() {
    return ComponentLocator.of(Button.class).byText("Search").locate();
  }

  private static TextField field() {
    return ComponentLocator.of(TextField.class).locate();
  }

  private static String understood() {
    return ComponentLocator.of(Paragraph.class).byClassName("tablesearch__understood").locate()
        .getText();
  }

  @SuppressWarnings("unchecked")
  private static long rows() {
    return ComponentLocator.of(Table.class).locate().getRepository().findAll().count();
  }
}
