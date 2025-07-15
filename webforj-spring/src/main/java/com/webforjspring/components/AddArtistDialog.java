package com.webforjspring.components;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.NumberField;
import com.webforj.component.field.TextField;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.H2;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optioninput.CheckBox;
import com.webforj.component.toast.Toast;
import com.webforjspring.entity.MusicArtist;
import com.webforjspring.service.MusicArtistService;

/**
 * Dialog component for adding new music artists
 */
public class AddArtistDialog extends Composite<Dialog> {
    
    private Dialog self = getBoundComponent();

    private final MusicArtistService artistService;
    private final Runnable onSaveCallback;
    
    // Form fields
    private TextField nameField;
    private TextField genreField;
    private TextField countryField;
    private NumberField yearFormedField;
    private CheckBox isActiveField;
    private TextArea biographyField;
    
    // Buttons
    private Button saveButton;
    private Button cancelButton;
    
    public AddArtistDialog(MusicArtistService artistService, Runnable onSaveCallback) {
        super();
        this.artistService = artistService;
        this.onSaveCallback = onSaveCallback;
        
        initializeDialog();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeDialog() {
        self.addToHeader(new H2("Add New Artist"));
        self.addClassName("add-artist-dialog");
    }
    
    private void initializeComponents() {
        // Form fields
        nameField = new TextField()
                .setLabel("Artist Name")
                .setPlaceholder("Enter artist or band name")
                .setRequired(true);
        
        genreField = new TextField()
                .setLabel("Genre")
                .setPlaceholder("e.g., Rock, Pop, Jazz");
        
        countryField = new TextField()
                .setLabel("Country")
                .setPlaceholder("Country of origin");
        
        yearFormedField = new NumberField()
                .setLabel("Year Formed")
                .setPlaceholder("Year artist/band was formed");
        
        isActiveField = new CheckBox("Currently Active")
                .setChecked(true);
        
        biographyField = new TextArea()
                .setLabel("Biography")
                .setPlaceholder("Brief biography (optional)")
                .setRows(3);
        
        // Buttons
        saveButton = new Button("Save Artist")
                .setTheme(ButtonTheme.PRIMARY);
        
        cancelButton = new Button("Cancel")
                .setTheme(ButtonTheme.DEFAULT);
    }
    
    private void setupLayout() {
        FlexLayout content = new FlexLayout();
        content.setDirection(FlexDirection.COLUMN);
        content.addClassName("dialog-content");
        
        // Form section
        FlexLayout form = new FlexLayout();
        form.setDirection(FlexDirection.COLUMN);
        form.addClassName("dialog-form");
        
        form.add(
            nameField,
            genreField,
            countryField,
            yearFormedField,
            isActiveField,
            biographyField
        );
        
        // Button section
        FlexLayout buttonBar = new FlexLayout();
        buttonBar.setDirection(FlexDirection.ROW);
        buttonBar.addClassName("dialog-buttons");
        
        buttonBar.add(cancelButton, saveButton);
        
        content.add(form, buttonBar);
        self.add(content);
    }
    
    private void setupEventHandlers() {
        // Save button handler
        saveButton.addClickListener(e -> {
            if (validateForm()) {
                saveArtist();
            }
        });
        
        // Cancel button handler
        cancelButton.addClickListener(e -> {
            self.close();
        });
        
        // Close dialog on escape key
        self.addCloseListener(e -> {
            clearForm();
        });
    }
    
    private boolean validateForm() {
        boolean isValid = true;
        
        // Validate required name field
        if (nameField.getValue() == null || nameField.getValue().trim().isEmpty()) {
            nameField.setInvalid(true);
            isValid = false;
        } else {
            nameField.setInvalid(false);
        }
        
        // Validate year (if provided)
        if (yearFormedField.getValue() != null) {
            Double year = yearFormedField.getValue();
            if (year < 1900 || year > 2024) {
                yearFormedField.setInvalid(true);
                isValid = false;
            } else {
                yearFormedField.setInvalid(false);
            }
        }
        
        return isValid;
    }
    
    private void saveArtist() {
        try {
            // Create new artist from form data
            MusicArtist newArtist = new MusicArtist();
            newArtist.setName(nameField.getValue().trim());
            newArtist.setGenre(genreField.getValue());
            newArtist.setCountry(countryField.getValue());
            
            // Handle year field (convert Double to Integer)
            if (yearFormedField.getValue() != null) {
                newArtist.setYearFormed(yearFormedField.getValue().intValue());
            }
            
            newArtist.setIsActive(isActiveField.isChecked());
            newArtist.setBiography(biographyField.getValue());
            
            // Save through service
            artistService.createArtist(newArtist);
            
            // Show success message
            Toast.show("Artist '" + newArtist.getName() + "' added successfully!");
            
            // Callback to refresh table
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            
            // Close dialog
            self.close();
            
        } catch (Exception ex) {
            // Show error message
            Toast.show("Error saving artist: " + ex.getMessage());
            System.err.println("Error saving artist: " + ex.getMessage());
        }
    }
    
    private void clearForm() {
        nameField.setValue("");
        genreField.setValue("");
        countryField.setValue("");
        yearFormedField.setValue(null);
        isActiveField.setChecked(true);
        biographyField.setValue("");
        
        // Clear any validation errors
        nameField.setInvalid(false);
        yearFormedField.setInvalid(false);
    }
    
    public void showDialog() {
        clearForm();
        self.open();
    }
}