package com.webforj.builtwithwebforj.crud.views;

import com.webforj.Debouncer;
import com.webforj.builtwithwebforj.crud.components.ArtistDialog;
import com.webforj.builtwithwebforj.crud.entity.MusicArtist;
import com.webforj.builtwithwebforj.crud.service.MusicArtistService;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.avatar.AvatarTheme;
import com.webforj.component.badge.BadgeTheme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Column.PinDirection;
import com.webforj.component.table.Table;
import com.webforj.component.table.renderer.AvatarRenderer;
import com.webforj.component.table.renderer.BadgeRenderer;
import com.webforj.component.table.renderer.BooleanRenderer;
import com.webforj.component.table.renderer.CompositeRenderer;
import com.webforj.component.table.renderer.IconButtonRenderer;
import com.webforj.component.table.renderer.TextRenderer;
import com.webforj.data.repository.spring.SpringDataRepository;
import com.webforj.router.annotation.Route;
import java.util.EnumSet;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/**
 * Lists the music artists in a table with search, add, edit and delete.
 */
@Route("/")
public class MusicArtistsView extends Composite<FlexLayout> {

  private final SpringDataRepository<MusicArtist, Long> repository;
  private final TextField searchField = new TextField();
  private final Button addButton = new Button("Add New", ButtonTheme.PRIMARY);
  private final Paragraph count = new Paragraph();
  private final Table<MusicArtist> artistTable = new Table<>();
  private final ArtistDialog artistDialog;
  // Waits until typing pauses, so one search runs per term and not one per keystroke.
  private final Debouncer searchDebouncer = new Debouncer(0.3f);

  public MusicArtistsView(MusicArtistService artistService) {
    repository = artistService.getFilterableRepository();
    artistDialog = new ArtistDialog(artistService, this::refresh);

    searchField.setPlaceholder("Search artists...");
    searchField.setPrefixComponent(TablerIcon.create("search"));
    searchField.onModify(e -> searchDebouncer.run(() -> search(searchField.getText())));

    addButton.setPrefixComponent(TablerIcon.create("plus"));
    addButton.onClick(e -> artistDialog.showDialog());

    count.addClassName("artists__count");

    FlexLayout toolbar = new FlexLayout(searchField, count, addButton);
    toolbar.addClassName("artists__toolbar");
    toolbar.setSpacing("var(--dwc-space-m)");
    toolbar.setWrap(FlexWrap.WRAP);
    toolbar.setAlignment(FlexAlignment.CENTER);

    setupTable();

    getBoundComponent().setDirection(FlexDirection.COLUMN).setSpacing("var(--dwc-space-m)")
        .addClassName("artists")
        .add(new H2("Music Artists"), toolbar, artistTable, artistDialog);
    updateCount();
  }

  @Override
  protected void onDidDestroy() {
    searchDebouncer.cancel();
  }

  private void setupTable() {
    // The database sorts the rows, so each sortable column names the entity property to order by.
    // Only the Artist column grows and the others keep a fixed width. The columns need about
    // 740px together, so the table scrolls sideways only on a window narrower than that.
    AvatarRenderer<MusicArtist> avatar = new AvatarRenderer<>();
    avatar.setTheme(AvatarTheme.PRIMARY);

    TextRenderer<MusicArtist> name = new TextRenderer<>();
    name.setDecorations(EnumSet.of(TextRenderer.TextDecoration.BOLD));

    CompositeRenderer<MusicArtist> artist = new CompositeRenderer<>(avatar, name);
    artist.setSpacing("var(--dwc-space-s)");

    artistTable.addColumn("artist", MusicArtist::getName).setLabel("Artist")
        .setRenderer(artist).setSortable(true).setPropertyName("name")
        .setFlex(1f).setMinWidth(180f);
    artistTable.addColumn("genre", MusicArtist::getGenre).setLabel("Genre")
        .setRenderer(new BadgeRenderer<MusicArtist>(BadgeTheme.OUTLINED_PRIMARY))
        .setSortable(true).setWidth(130f);
    artistTable.addColumn("country", MusicArtist::getCountry).setLabel("Country")
        .setSortable(true).setWidth(150f);
    artistTable.addColumn("yearFormed", MusicArtist::getYearFormed).setLabel("Year Formed")
        .setSortable(true).setWidth(130f);
    artistTable.addColumn("active", MusicArtist::getIsActive).setLabel("Active")
        .setRenderer(new BooleanRenderer<MusicArtist>(
            TablerIcon.create("circle-check").setTheme(Theme.SUCCESS),
            TablerIcon.create("circle-x").setTheme(Theme.GRAY)))
        .setSortable(true).setPropertyName("isActive").setWidth(90f);
    artistTable.addColumn("edit", a -> "").setLabel("")
        .setRenderer(new IconButtonRenderer<MusicArtist>(TablerIcon.create("edit"),
            e -> artistDialog.showDialog(e.getItem())).setAttribute("aria-label", "Edit artist"))
        .setWidth(56f).setPinDirection(PinDirection.RIGHT);

    artistTable.addClassName("artists__table");
    artistTable.setRowHeight(48);
    artistTable.setSelectionMode(Table.SelectionMode.SINGLE);
    artistTable.onItemDoubleClick(e -> artistDialog.showDialog(e.getItem()));
    artistTable.setRepository(repository);
  }

  private void search(String text) {
    if (text == null || text.isBlank()) {
      repository.setBaseFilter((Specification<MusicArtist>) null);
    } else {
      // The typed text is matched literally, so the LIKE wildcards in it are escaped.
      String literal = text.trim().toLowerCase(Locale.ROOT).replace("\\", "\\\\")
          .replace("%", "\\%").replace("_", "\\_");
      String term = "%" + literal + "%";
      Specification<MusicArtist> matches = (root, query, cb) -> cb.or(
          cb.like(cb.lower(root.get("name")), term, '\\'),
          cb.like(cb.lower(root.get("genre")), term, '\\'),
          cb.like(cb.lower(root.get("country")), term, '\\'));
      repository.setBaseFilter(matches);
    }
    refresh();
  }

  private void refresh() {
    repository.commit();
    updateCount();
  }

  private void updateCount() {
    long shown = repository.size();
    count.setText(shown == 1 ? "1 artist" : shown + " artists");
  }
}
