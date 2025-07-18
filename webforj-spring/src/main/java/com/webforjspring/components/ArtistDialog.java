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
import com.webforj.component.optiondialog.ConfirmDialog;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;
import com.webforjspring.entity.MusicArtist;
import com.webforjspring.service.MusicArtistService;

/**
 * Dialog component for adding and editing music artists
 */
public class ArtistDialog extends Composite<Dialog> {

	public enum Mode {
		ADD, EDIT
	}

	private Mode currentMode = Mode.ADD;

	private Dialog self = getBoundComponent();

	private final MusicArtistService artistService;
	private final Runnable onSaveCallback;

	// Data binding context
	private BindingContext<MusicArtist> bindingContext;
	private MusicArtist artist;

	private H2 title;
	private TextField name;
	private TextField genre;
	private TextField country;
	private NumberField yearFormed;
	private CheckBox isActive;
	private TextArea biography;

	private Button saveButton;
	private Button cancelButton;
	private Button deleteButton;

	public ArtistDialog(MusicArtistService artistService, Runnable onSaveCallback) {
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
		title = new H2("Add New Artist");
		self.addToHeader(title);
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

		deleteButton = new Button("Delete Artist")
				.setTheme(ButtonTheme.DANGER)
				.setVisible(false); // Initially hidden for new artists
	}

	private void setupDataBinding() {
		bindingContext = BindingContext.of(this, MusicArtist.class, true);
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

		buttonBar.add(saveButton, deleteButton, cancelButton);

		content.add(form, buttonBar);
		self.add(content);
	}

	private void setupEventHandlers() {
		// Save button handler
		saveButton.addClickListener(e -> saveArtist());

		// Cancel button handler
		cancelButton.addClickListener(e -> self.close());

		// Delete button handler
		deleteButton.addClickListener(e -> deleteArtist());

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

			// Call appropriate service method based on mode
			if (currentMode == Mode.ADD) {
				artistService.createArtist(artist);
				Toast.show("Artist '" + artist.getName() + "' added successfully!", Theme.SUCCESS);
			} else {
				artistService.updateArtist(artist);
				Toast.show("Artist '" + artist.getName() + "' updated successfully!", Theme.SUCCESS);
			}

			// Callback to refresh table
			if (onSaveCallback != null) {
				onSaveCallback.run();
			}

			// Close dialog
			self.close();

		} catch (Exception ex) {
			// Show error message
			String action = currentMode == Mode.ADD ? "saving" : "updating";
			Toast.show("Error " + action + " artist: " + ex.getMessage(), Theme.DANGER);
		}
	}

	private void updateDialogForMode() {
		if (currentMode == Mode.ADD) {
			// Update header
			title.setText("Add New Artist");
			saveButton.setText("Save Artist");
			deleteButton.setVisible(false); // Hide delete button for new artists
		} else {
			// Update header
			title.setText("Edit Artist");
			saveButton.setText("Update Artist");
			deleteButton.setVisible(true); // Show delete button for existing artists
		}
	}

	private void deleteArtist() {
		if (currentMode == Mode.EDIT && artist != null) {
			ConfirmDialog dialog = new ConfirmDialog(
				"Are you sure you want to delete '" + artist.getName() + "'? This action cannot be undone.",
				"Delete Artist",
				ConfirmDialog.OptionType.YES_NO,
				ConfirmDialog.MessageType.QUESTION
			);
			
			dialog.setTheme(Theme.DANGER);
			dialog.setButtonTheme(ConfirmDialog.Button.FIRST, ButtonTheme.DANGER);
			dialog.setButtonTheme(ConfirmDialog.Button.SECOND, ButtonTheme.OUTLINED_GRAY);
			
			ConfirmDialog.Result result = dialog.show();
			
			if (result == ConfirmDialog.Result.YES) {
				try {
					artistService.deleteArtist(artist.getId());
					Toast.show("Artist '" + artist.getName() + "' deleted successfully!", Theme.SUCCESS);
					
					// Callback to refresh table
					if (onSaveCallback != null) {
						onSaveCallback.run();
					}
					
					// Close dialog
					self.close();
					
				} catch (Exception ex) {
					Toast.show("Error deleting artist: " + ex.getMessage(), Theme.DANGER);
				}
			}
		}
	}

	private void resetForm() {
		if (currentMode == Mode.ADD) {
			// Create new artist entity with default values
			artist = new MusicArtist();
			artist.setIsActive(true); // Set default active state
		}
		// For EDIT mode, we already have the artist set

		// Read the entity data into form fields using binding context
		bindingContext.read(artist);

		// No manual conversion needed - NumberField handles Integer conversion automatically
	}

	public void showDialog() {
		this.currentMode = Mode.ADD;
		resetForm();
		updateDialogForMode();
		self.open();
	}

	public void showDialog(MusicArtist existingArtist) {
		this.currentMode = Mode.EDIT;
		this.artist = existingArtist;
		bindingContext.read(artist);
		updateDialogForMode();
		self.open();
	}
}