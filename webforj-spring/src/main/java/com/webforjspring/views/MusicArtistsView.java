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
import com.webforj.component.toast.Toast;
import com.webforj.router.annotation.Route;
import com.webforjspring.entity.MusicArtist;
import com.webforjspring.service.MusicArtistService;
import com.webforjspring.components.AddArtistDialog;
import com.webforjspring.components.renderers.ArtistAvatarRenderer;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Route("/")
public class MusicArtistsView extends Composite<FlexLayout> {

    @Autowired
    private MusicArtistService artistService;

    // Main layout components
    private FlexLayout container = getBoundComponent();
    private FlexLayout header;
    private FlexLayout toolbar;
    private FlexLayout tableContainer;
    
    // UI Components
    private H1 pageTitle;
    private Button addButton;
    private Button refreshButton;
    private TextField searchField;
    private Table<MusicArtist> artistTable;
    private AddArtistDialog addArtistDialog;

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
        
        refreshButton = new Button("Refresh")
                .setTheme(ButtonTheme.DEFAULT)
                .setPrefixComponent(FeatherIcon.REFRESH_CW.create());
        
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
        leftToolbar.add(addButton, refreshButton);
        
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
        
        // Style the table with CSS class
        artistTable.addClassName("artists-table");
        artistTable.setRowHeight(45);
    }

    private void setupEventHandlers() {
        // Add button click handler
        addButton.addClickListener(e -> {
            addArtistDialog.showDialog();
        });
        
        // Refresh button click handler
        refreshButton.addClickListener(e -> {
            loadData();
            Toast.show("Data refreshed!");
        });
        
        // Search field handler
        searchField.addModifyListener(e -> {
            String searchTerm = searchField.getValue();
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                loadData();
            } else {
                searchArtists(searchTerm);
            }
        });
    }

    private void loadData() {
        try {
            List<MusicArtist> artists = artistService.getAllArtists();
            artistTable.setItems(artists);
            
            // Show count in console for debugging
            System.out.println("Loaded " + artists.size() + " artists");
        } catch (Exception e) {
            Toast.show("Error loading artists: " + e.getMessage());
            System.err.println("Error loading artists: " + e.getMessage());
        }
    }

    private void searchArtists(String searchTerm) {
        try {
            List<MusicArtist> artists = artistService.searchArtistsByName(searchTerm);
            artistTable.setItems(artists);
            
            // Show search results count
            System.out.println("Found " + artists.size() + " artists matching '" + searchTerm + "'");
        } catch (Exception e) {
            Toast.show("Error searching artists: " + e.getMessage());
            System.err.println("Error searching artists: " + e.getMessage());
        }
    }
    
    private void initializeDialog() {
        // Initialize add artist dialog with callback to refresh table
        addArtistDialog = new AddArtistDialog(artistService, this::loadData);
        container.add(addArtistDialog);
    }
}