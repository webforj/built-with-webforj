package com.webforj.builtwithwebforj.crud.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.act.component.table.TableInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.crud.entity.MusicArtist;
import com.webforj.builtwithwebforj.crud.repository.MusicArtistRepository;
import com.webforj.component.Component;
import com.webforj.component.button.Button;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.NumberField;
import com.webforj.component.field.TextField;
import com.webforj.component.optiondialog.ConfirmDialog;
import com.webforj.component.table.Table;
import com.webforj.component.toast.Toast;
import com.webforj.data.repository.OrderCriteria.Direction;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.time.Year;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Drives the view the way a user does, without a browser or a server. Each test opens the view,
// finds components with ComponentLocator and acts on them with UserInteraction.
@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = MusicArtistsView.class)
class MusicArtistsViewTest {

  @Autowired
  private MusicArtistRepository artists;

  @BeforeEach
  void seed() {
    artists.deleteAll();
    artists.save(artist("The Beatles", "Rock", "United Kingdom", 1960));
    artists.save(artist("Elvis Presley", "Rock", "United States", 1954));
    artists.save(artist("Adele", "Pop", "United Kingdom", 2006));
  }

  @Test
  void shouldListEveryArtist(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    assertEquals(List.of("The Beatles", "Elvis Presley", "Adele"), names());
  }

  @Test
  void shouldSearchOnceTypingPauses(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    UserInteraction.type(search(), "united kingdom");
    // The search is debounced, so nothing has happened yet.
    assertEquals(3, names().size());

    // Lets the debounce delay elapse.
    context.runIntervals();
    assertEquals(List.of("The Beatles", "Adele"), names());
  }

  @Test
  void shouldSortByAColumn(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);
    Table<MusicArtist> table = table();

    UserInteraction.perform(table,
        TableInteraction.sort(Map.of(table.getColumnById("yearFormed"), Direction.ASC)));

    assertEquals(List.of("Elvis Presley", "The Beatles", "Adele"), names());
  }

  @Test
  void shouldAddAnArtist(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    UserInteraction.click(button("Add New"));
    fillForm("Queen", "Rock", "United Kingdom", 1970);
    UserInteraction.click(button("Save Artist"));

    assertFalse(dialog().isOpened());
    assertEquals("Artist 'Queen' added successfully!", toast());
    assertTrue(names().contains("Queen"));
  }

  @Test
  void shouldRejectAYearInTheFuture(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    UserInteraction.click(button("Add New"));
    fillForm("Queen", "Rock", "United Kingdom", Year.now().getValue() + 1);
    UserInteraction.click(button("Save Artist"));

    // The entity's validation constraints mark the field and the dialog stays open.
    assertTrue(dialog().isOpened());
    assertTrue(field(NumberField.class, "Year Formed").isInvalid());
    assertEquals(3, artists.count());
  }

  @Test
  void shouldEditAnArtistOpenedFromItsRow(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    UserInteraction.doubleClick(table(), row("Elvis Presley"));
    assertEquals("Elvis Presley", field(TextField.class, "Artist Name").getValue());

    UserInteraction.type(field(TextField.class, "Genre"), "Rock and Roll");
    UserInteraction.click(button("Update Artist"));

    assertFalse(dialog().isOpened());
    assertEquals("Rock and Roll", row("Elvis Presley").getGenre());
  }

  @Test
  void shouldDeleteAnArtistOnceTheUserConfirms(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    UserInteraction.doubleClick(table(), row("Adele"));
    // The answer is queued before the click, because the confirm dialog blocks until answered.
    context.answerConfirmDialog(ConfirmDialog.Result.YES);
    UserInteraction.click(button("Delete Artist"));

    assertEquals(List.of("The Beatles", "Elvis Presley"), names());
  }

  @Test
  void shouldKeepTheArtistWhenTheUserDeclines(WebforjTestbed context) {
    context.navigate(MusicArtistsView.class);

    UserInteraction.doubleClick(table(), row("Adele"));
    context.answerConfirmDialog(ConfirmDialog.Result.NO);
    UserInteraction.click(button("Delete Artist"));

    assertEquals(3, artists.count());
  }

  private static MusicArtist artist(String name, String genre, String country, int year) {
    MusicArtist artist = new MusicArtist(name);
    artist.setGenre(genre);
    artist.setCountry(country);
    artist.setYearFormed(year);
    return artist;
  }

  private static void fillForm(String name, String genre, String country, int year) {
    UserInteraction.type(field(TextField.class, "Artist Name"), name);
    UserInteraction.type(field(TextField.class, "Genre"), genre);
    UserInteraction.type(field(TextField.class, "Country"), country);
    UserInteraction.type(field(NumberField.class, "Year Formed"), String.valueOf(year));
  }

  @SuppressWarnings("unchecked")
  private static Table<MusicArtist> table() {
    return ComponentLocator.of(Table.class).locate();
  }

  /**
   * The artist names in the order the table shows them.
   */
  private static List<String> names() {
    return table().getRepository().findAll().map(MusicArtist::getName).toList();
  }

  private static MusicArtist row(String name) {
    return table().getRepository().findAll().filter(a -> a.getName().equals(name)).findFirst()
        .orElseThrow();
  }

  private static TextField search() {
    return ComponentLocator.of(TextField.class).byPlaceholder("Search artists...").locate();
  }

  private static Button button(String text) {
    return ComponentLocator.of(Button.class).byText(text).locate();
  }

  private static <T extends Component> T field(Class<T> type, String label) {
    return ComponentLocator.of(type).byLabel(label).locate();
  }

  private static Dialog dialog() {
    return ComponentLocator.of(Dialog.class).locate();
  }

  private static String toast() {
    return ComponentLocator.of(Toast.class).last().orElseThrow().getText();
  }
}
