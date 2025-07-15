package com.webforjspring.components;

import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.TextField;
import com.webforj.component.field.NumberField;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.H2;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optioninput.CheckBox;
import com.webforj.component.toast.Toast;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;
import com.webforjspring.entity.MusicArtist;
import com.webforjspring.service.MusicArtistService;

/**
 * Dialog component for adding new music artists
 */
public class AddArtistDialog extends Composite<Dialog> {

	private Dialog self = getBoundComponent();

	private final MusicArtistService artistService;
	private final Runnable onSaveCallback;

	// Data binding context
	private BindingContext<MusicArtist> bindingContext;
	private MusicArtist artist;

	// Form fields - names must match entity properties for automatic binding
	private TextField name;
	private TextField genre;
	private TextField country;
	private NumberField yearFormed;
	private CheckBox isActive;
	private TextArea biography;

	// Buttons
	private Button saveButton;
	private Button cancelButton;

	public AddArtistDialog(MusicArtistService artistService, Runnable onSaveCallback) {
		super();
		this.artistService = artistService;
		this.onSaveCallback = onSaveCallback;

		initializeDialog();
		initializeComponents();
		setupDataBinding();
		setupLayout();
		setupEventHandlers();
	}

	private void initializeDialog() {
		self.addToHeader(new H2("Add New Artist"));
		self.addClassName("add-artist-dialog");
	}

	private void initializeComponents() {
		// Form fields - field names must match entity properties
		name = new TextField()
				.setLabel("Artist Name")
				.setPlaceholder("Enter artist or band name");

		genre = new TextField()
				.setLabel("Genre")
				.setPlaceholder("e.g., Rock, Pop, Jazz");

		country = new TextField()
				.setLabel("Country")
				.setPlaceholder("Country of origin");

		yearFormed = new NumberField()
				.setLabel("Year Formed")
				.setPlaceholder("Year artist/band was formed");

		isActive = new CheckBox("Currently Active");

		biography = new TextArea()
				.setLabel("Biography")
				.setPlaceholder("Brief biography (optional)")
				.setRows(3);

		// Buttons
		saveButton = new Button("Save Artist")
				.setTheme(ButtonTheme.PRIMARY);

		cancelButton = new Button("Cancel")
				.setTheme(ButtonTheme.DEFAULT);
	}

	private void setupDataBinding() {
		// Create binding context with automatic binding enabled
		// The true parameter enables Jakarta validation
		bindingContext = BindingContext.of(this, MusicArtist.class, true);

		// With automatic binding, field names match property names
		// Jakarta validation annotations on the entity will be automatically applied
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
				name,
				genre,
				country,
				yearFormed,
				isActive,
				biography);

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
			saveArtist();
		});

		// Cancel button handler
		cancelButton.addClickListener(e -> {
			self.close();
		});

		// Close dialog on escape key
		self.addCloseListener(e -> {
			resetForm();
		});
	}

	private void saveArtist() {
		try {
			// Validate and write form data to entity using binding context
			ValidationResult validationResult = bindingContext.write(artist);

			if (!validationResult.isValid()) {
				// Jakarta validation automatically sets field-level errors
				// The binding context will display errors on each field
				// Optionally show a general message
				String errorCount = validationResult.getMessages().size() +
						(validationResult.getMessages().size() == 1 ? " error" : " errors");
				Toast.show("Please fix " + errorCount + " before saving", Theme.DANGER);
				return;
			}

			// No manual conversion needed - NumberField handles Integer conversion automatically

			// Save through service
			artistService.createArtist(artist);

			// Show success message
			Toast.show("Artist '" + artist.getName() + "' added successfully!", Theme.SUCCESS);

			// Callback to refresh table
			if (onSaveCallback != null) {
				onSaveCallback.run();
			}

			// Close dialog
			self.close();

		} catch (Exception ex) {
			// Show error message
			Toast.show("Error saving artist: " + ex.getMessage(), Theme.DANGER);
		}
	}

	private void resetForm() {
		// Create new artist entity with default values
		artist = new MusicArtist();
		artist.setIsActive(true); // Set default active state

		// Read the entity data into form fields using binding context
		bindingContext.read(artist);

		// No manual conversion needed - NumberField handles Integer conversion automatically
	}

	public void showDialog() {
		resetForm();
		self.open();
	}
}