package com.webforjspring.views;

import com.webforj.App;
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
import com.webforj.component.table.renderer.IconRenderer;
import com.webforj.component.toast.Toast;
import com.webforj.component.Theme;
import com.webforj.data.repository.CollectionRepository;
import com.webforj.data.repository.spring.SpringDataRepository;
import com.webforj.router.annotation.Route;
import com.webforjspring.entity.MusicArtist;
import com.webforjspring.repository.MusicArtistRepository;
import com.webforjspring.service.MusicArtistService;
import com.webforjspring.components.ArtistDialog;
import com.webforjspring.components.renderers.ArtistAvatarRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import jakarta.annotation.PostConstruct;

@Route("/")
public class MusicArtistsView extends Composite<FlexLayout> {

    @Autowired
    private MusicArtistService artistService;

    @Autowired
    private MusicArtistRepository artistRepository;
    private CollectionRepository<MusicArtist> testRepo;

    private SpringDataRepository<MusicArtist, Long> repository;

    // Main layout components
    private FlexLayout container = getBoundComponent();
    private FlexLayout header;
    private FlexLayout toolbar;
    private FlexLayout tableContainer;

    // UI Components
    private H1 pageTitle;
    private Button addButton;
    private TextField searchField;
    private Table<MusicArtist> artistTable;
    private ArtistDialog artistDialog;

    public MusicArtistsView() {
        // Note: We can't call methods that use artistService here yet
        // because @Autowired happens after constructor
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        // We'll move loadData() to @PostConstruct
    }

    @PostConstruct
    private void init() {
        // This runs after @Autowired injection is complete
        initializeDialog();
        loadData();
    }

    private void initializeComponents() {
        // Page title
        pageTitle = new H1("Music Artists Management");

        // Toolbar buttons
        addButton = new Button("Add New Artist")
                .setTheme(ButtonTheme.PRIMARY)
                .setPrefixComponent(FeatherIcon.PLUS.create());

        // Search field
        searchField = new TextField()
                .setPlaceholder("Search artists...")
                .setPrefixComponent(FeatherIcon.SEARCH.create());

        // Table component
        artistTable = new Table<>();
        setupTableColumns();
    }

    private void setupLayout() {
        // Main container setup
        container.setDirection(FlexDirection.COLUMN);
        container.addClassName("main-container");

        // Header section
        header = new FlexLayout();
        header.setDirection(FlexDirection.COLUMN);
        header.addClassName("page-header");

        // Toolbar section
        toolbar = new FlexLayout();
        toolbar.setDirection(FlexDirection.ROW);
        toolbar.setWrap(FlexWrap.WRAP);
        toolbar.addClassName("toolbar");

        // Left side of toolbar (action buttons)
        FlexLayout leftToolbar = new FlexLayout();
        leftToolbar.setDirection(FlexDirection.ROW);
        leftToolbar.addClassName("toolbar-left");
        leftToolbar.add(addButton);

        // Right side of toolbar (search)
        FlexLayout rightToolbar = new FlexLayout();
        rightToolbar.setDirection(FlexDirection.ROW);
        rightToolbar.add(searchField);

        toolbar.add(leftToolbar, rightToolbar);

        // Table container
        tableContainer = new FlexLayout();
        tableContainer.setDirection(FlexDirection.COLUMN);
        tableContainer.addClassName("table-container");

        // Add components to layout
        header.add(pageTitle);
        tableContainer.add(artistTable);
        container.add(header, toolbar, tableContainer);
    }

    private void setupTableColumns() {
        // Artist column with beautiful avatar renderer
        artistTable.addColumn("Name", MusicArtist::getName).setHidden(true);
        artistTable.addColumn("Artist", new ArtistAvatarRenderer())
                .setMinWidth(200.0f);

        // Other columns
        artistTable.addColumn("Genre", MusicArtist::getGenre);
        artistTable.addColumn("Country", MusicArtist::getCountry);
        artistTable.addColumn("Year Formed", MusicArtist::getYearFormed);
        artistTable.addColumn("Active", artist -> artist.getIsActive() ? "✓" : "✗");
        artistTable.addColumn("", new IconRenderer<MusicArtist>("edit", "feather", e -> {
            MusicArtist artist = e.getItem();
            artistDialog.showDialog(artist);
        }))
                .setMinWidth(50.0f);

        artistTable.addClassName("artists-table");
        artistTable.setRowHeight(45);
        artistTable.setSelectionMode(Table.SelectionMode.SINGLE);
    }

    private void setupEventHandlers() {
        // Add button click handler
        addButton.addClickListener(e -> {
            artistDialog.showDialog();
        });

        // Search field handler
        searchField.onModify(e -> {
            String searchTerm = searchField.getValue();

            if (repository == null) {
                return;
            }

            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                // Clear filter - show all artists
                repository.setFilter((Specification<MusicArtist>) null);
            } else {
                // Create specification for multi-field search
                String term = searchTerm.trim().toLowerCase();
                Specification<MusicArtist> searchSpec = (root, query, cb) -> cb.or(
                        cb.like(cb.lower(root.get("name")), "%" + term + "%"),
                        cb.like(cb.lower(root.get("genre")), "%" + term + "%"),
                        cb.like(cb.lower(root.get("country")), "%" + term + "%"));

                repository.setFilter(searchSpec);
            }
            repository.commit(); // Refresh the table to apply the filter
        });
    }

    private void loadData() {
        try {
            // Create SpringDataRepository wrapper
            repository = new SpringDataRepository<>(artistRepository);

            // Bind repository to table (no filter initially)
            artistTable.setRepository(repository);
        } catch (Exception e) {
            Toast.show("Error loading artists: " + e.getMessage());
            System.err.println("Error loading artists: " + e.getMessage());
        }
    }


    private void initializeDialog() {
        artistDialog = new ArtistDialog(artistService, () -> {
            if (repository != null) {
                repository.commit(); // Refresh the table after adding
            }
        });
        container.add(artistDialog);
    }
}