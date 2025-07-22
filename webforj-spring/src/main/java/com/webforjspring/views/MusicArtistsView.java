package com.webforjspring.views;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.table.Table;
import com.webforj.component.table.Column.PinDirection;
import com.webforj.component.table.renderer.IconRenderer;
import com.webforj.data.repository.spring.SpringDataRepository;
import com.webforj.router.annotation.Route;
import com.webforjspring.entity.MusicArtist;
import com.webforjspring.service.MusicArtistService;
import com.webforjspring.components.ArtistDialog;
import com.webforjspring.components.renderers.ArtistAvatarRenderer;

import org.springframework.data.jpa.domain.Specification;

/**
 * Main view for managing music artists.
 * Provides a responsive table interface with search, add, edit, and delete functionality.
 */
@Route("/")
public class MusicArtistsView extends Composite<FlexLayout> {

    private final MusicArtistService artistService;

    private SpringDataRepository<MusicArtist, Long> repository;

    private FlexLayout container = getBoundComponent();
    private FlexLayout header;
    private FlexLayout toolbar;
    private FlexLayout tableContainer;

    private H1 pageTitle;
    private Button addButton;
    private TextField searchField;
    private Table<MusicArtist> artistTable;
    private ArtistDialog artistDialog;

    /**
     * Constructs a new MusicArtistsView with the specified service.
     * 
     * @param artistService the service for managing music artists
     */
    public MusicArtistsView(MusicArtistService artistService) {
        this.artistService = artistService;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        initializeDialog();
        loadData();
    }

    /**
     * Initializes all UI components.
     */
    private void initializeComponents() {
        pageTitle = new H1("Music Artists Management");

        addButton = new Button("Add New Artist")
                .setTheme(ButtonTheme.PRIMARY)
                .setPrefixComponent(FeatherIcon.PLUS.create());

        searchField = new TextField()
                .setPlaceholder("Search artists...")
                .setPrefixComponent(FeatherIcon.SEARCH.create());

        artistTable = new Table<>();
        setupTableColumns();
    }

    /**
     * Sets up the overall page layout structure.
     */
    private void setupLayout() {
        container.setDirection(FlexDirection.COLUMN);
        container.addClassName("main-container");

        header = new FlexLayout();
        header.setDirection(FlexDirection.COLUMN);
        header.addClassName("page-header");

        toolbar = new FlexLayout();
        toolbar.setDirection(FlexDirection.ROW);
        toolbar.setWrap(FlexWrap.WRAP);
        toolbar.addClassName("toolbar");

        FlexLayout leftToolbar = new FlexLayout();
        leftToolbar.setDirection(FlexDirection.ROW);
        leftToolbar.addClassName("toolbar-left");
        leftToolbar.add(addButton);

        FlexLayout rightToolbar = new FlexLayout();
        rightToolbar.setDirection(FlexDirection.ROW);
        rightToolbar.add(searchField);

        toolbar.add(leftToolbar, rightToolbar);

        tableContainer = new FlexLayout();
        tableContainer.setDirection(FlexDirection.COLUMN);
        tableContainer.addClassName("table-container");

        header.add(pageTitle);
        tableContainer.add(artistTable);
        container.add(header, toolbar, tableContainer);
    }

    /**
     * Configures table columns and appearance.
     */
    private void setupTableColumns() {
        artistTable.addColumn("Name", MusicArtist::getName).setHidden(true);
        artistTable.addColumn("Artist", new ArtistAvatarRenderer())
                .setMinWidth(200.0f);

        artistTable.addColumn("Genre", MusicArtist::getGenre);
        artistTable.addColumn("Country", MusicArtist::getCountry);
        artistTable.addColumn("Year Formed", MusicArtist::getYearFormed);
        artistTable.addColumn("Active", artist -> artist.getIsActive() ? "✓" : "✗");
        artistTable.addColumn("", new IconRenderer<MusicArtist>("edit", "feather", e -> {
            MusicArtist artist = e.getItem();
            artistDialog.showDialog(artist);
        }))
                .setMinWidth(50.0f)
                .setPinDirection(PinDirection.RIGHT);

        artistTable.addClassName("artists-table");
        artistTable.setRowHeight(45);
        artistTable.setSelectionMode(Table.SelectionMode.SINGLE);
    }

    /**
     * Sets up event handlers for user interactions.
     */
    private void setupEventHandlers() {
        addButton.addClickListener(e -> artistDialog.showDialog());

        searchField.onModify(e -> {
            String searchTerm = searchField.getValue();

            if (repository == null) {
                return;
            }

            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                repository.setFilter((Specification<MusicArtist>) null);
            } else {
                String term = searchTerm.trim().toLowerCase()
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
                    
                Specification<MusicArtist> searchSpec = (root, query, cb) -> cb.or(
                        cb.like(cb.lower(root.get("name")), "%" + term + "%"),
                        cb.like(cb.lower(root.get("genre")), "%" + term + "%"),
                        cb.like(cb.lower(root.get("country")), "%" + term + "%"));
                repository.setFilter(searchSpec);
            }
            repository.commit();
        });
    }

    /**
     * Loads artist data into the table.
     */
    private void loadData() {
        repository = artistService.getFilterableRepository();
        artistTable.setRepository(repository);
    }

    /**
     * Initializes the artist add/edit dialog.
     */
    private void initializeDialog() {
        artistDialog = new ArtistDialog(artistService, () -> {
            if (repository != null) {
                repository.commit();
            }
        });
        container.add(artistDialog);
    }
}