package com.webforj.bookstore.components;

import com.webforj.annotation.StyleSheet;
import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.service.GenreService;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.drawer.Drawer;
import com.webforj.component.drawer.Drawer.Placement;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H2;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optiondialog.ConfirmDialog;
import com.webforj.component.optiondialog.OptionDialog;
import com.webforj.component.table.Table;
import com.webforj.component.table.event.renderer.RendererClickEvent;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.table.renderer.Renderer;
import com.webforj.component.table.renderer.VoidElementRenderer;
import com.webforj.component.table.Column;

import com.webforj.data.repository.spring.SpringDataRepository;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import com.webforj.component.toast.Toast;

@StyleSheet("ws://drawer.css")
public class GenreDrawer extends Composite<Drawer> {

    private Drawer self = getBoundComponent();
    private GenreService genreService;

    private SpringDataRepository<Genre, String> repository;

    private Table<Genre> genreTable;
    private TextField searchField;
    private GenreDialog genreDialog;

    public GenreDrawer(GenreService genreService) {
        this.genreService = genreService;

        configureDrawer();
        createContent();

        genreDialog = new GenreDialog(this::saveGenre);

        repository = genreService.getFilterableRepository();
        genreTable.setRepository(repository);

        // Clear auto-generated columns
        new ArrayList<>(genreTable.getColumns()).forEach(genreTable::removeColumn);

        configureTableColumns();

        // Add dialog to window when drawer is attached
        self.whenAttached().thenAccept(e -> getWindow().add(genreDialog));

        self.close();
    }

    private void configureDrawer() {
        self.setPlacement(Placement.BOTTOM_CENTER);
        self.addClassName("bookstore-drawer");

        // Create title layout with icon and text aligned on baseline
        FlexLayout titleLayout = new FlexLayout();
        titleLayout.setDirection(FlexDirection.ROW);
        titleLayout.setSpacing("var(--dwc-space-s)");
        titleLayout.setAlignment(FlexAlignment.BASELINE);
        titleLayout.add(FeatherIcon.FOLDER.create(), new H2("Manage Genres"));

        self.addToTitle(titleLayout);
    }

    private void createContent() {
        FlexLayout container = new FlexLayout();
        container.setDirection(FlexDirection.COLUMN);
        container.setHeight("100%");

        // Header Area: Search and Add Button
        FlexLayout header = new FlexLayout();
        header.setJustifyContent(FlexJustifyContent.BETWEEN);
        header.setAlignment(FlexAlignment.CENTER);
        header.setSpacing("10px");
        header.setStyle("padding", "var(--dwc-space-s)");

        searchField = new TextField();
        searchField.setPlaceholder("Filter genres...");
        searchField.setPrefixComponent(TablerIcon.create("search"));
        searchField.onModify(e -> filterGenres(searchField.getText()));
        searchField.setStyle("flex", "1");

        Button addButton = new Button("Add", ButtonTheme.PRIMARY, e -> genreDialog.open());
        addButton.setPrefixComponent(FeatherIcon.PLUS.create());

        header.add(addButton, searchField);
        container.add(header);

        // Table
        genreTable = new Table<>();
        genreTable.addClassName("genre-table");
        genreTable.setRowHeight(40);

        container.add(genreTable);

        genreTable.setSelectionMode(Table.SelectionMode.NONE);

        self.add(container);

    }

    private void configureTableColumns() {
        // Color Column (Circle)
        genreTable.addColumn("color", Genre::getColor)
                .setRenderer(new Renderer<Genre>() {
                    @Override
                    public String build() {
                        return "<div style='width: 20px; height: 20px; border-radius: 50%; background-color: <%= cell.row.getValue('color') %>; border: 1px solid #ddd;'></div>";
                    }
                })
                .setSortable(false)
                .setWidth(35)
                .setAlignment(Column.Alignment.CENTER);

        // Name Column
        genreTable.addColumn("Name", Genre::getName)
                .setSortable(true)
                .setFlex(1f);

        // Delete Column (Red Trash Icon)
        VoidElementRenderer<Genre> deleteRenderer = new VoidElementRenderer<>("dwc-icon-button", this::onDeleteClick);
        deleteRenderer.setAttribute("name", "trash");
        deleteRenderer.setAttribute("theme", "danger");

        genreTable.addColumn(deleteRenderer)
                .setSortable(false)
                .setWidth(50)
                .setAlignment(Column.Alignment.CENTER);
    }

    public void open() {
        refreshTable();
        self.open();
    }

    private void refreshTable() {
        if (repository != null) {
            repository.commit();
        }
    }

    private void filterGenres(String query) {
        if (repository == null) {
            return;
        }

        if (query == null || query.isBlank()) {
            repository.setFilter((Specification<Genre>) null);
        } else {
            repository.setFilter((root, q, cb) -> cb.like(cb.lower(root.get("name")), "%" + query.toLowerCase() + "%"));
        }
        repository.commit();
    }

    private void saveGenre(Genre genre) {
        genreService.saveGenre(genre);
        refreshTable();
    }

    private void onDeleteClick(RendererClickEvent<Genre> e) {
        Genre genre = e.getItem();
        if (genre != null) {
            ConfirmDialog.Result result = OptionDialog.showConfirmDialog(
                    "Are you sure you want to delete '" + genre.getName() + "'?",
                    "Delete Genre",
                    ConfirmDialog.OptionType.YES_NO,
                    ConfirmDialog.MessageType.WARNING);

            if (result == ConfirmDialog.Result.YES) {
                genreService.deleteGenre(genre.getId());
                refreshTable();
                Toast.show("Genre deleted: " + genre.getName(),
                        Theme.GRAY);
            }
        }
    }
}
