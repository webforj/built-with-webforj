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
 * Dialog component for adding and editing music artists.
 * Provides a form interface for creating new artists or editing existing ones
 * with full data binding and validation support.
 */
public class ArtistDialog extends Composite<Dialog> {

	/**
	 * Enum representing the dialog mode
	 */
	public enum Mode {
		ADD, EDIT
	}

	private Mode currentMode = Mode.ADD;

	private Dialog self = getBoundComponent();

	private final MusicArtistService artistService;
	private final Runnable onSaveCallback;

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

	/**
	 * Constructs a new ArtistDialog.
	 * 
	 * @param artistService the service for managing artist data
	 * @param onSaveCallback callback to execute after successful save/delete operations
	 */
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

	/**
	 * Initializes the dialog properties.
	 */
	private void initializeDialog() {
		title = new H2("Add New Artist");
		self.addToHeader(title);
		self.addClassName("add-artist-dialog");
		self.setCancelOnOutsideClick(false);
	}

	/**
	 * Initializes all form components.
	 */
	private void initializeComponents() {
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

		saveButton = new Button("Save Artist")
				.setTheme(ButtonTheme.PRIMARY);

		cancelButton = new Button("Cancel")
				.setTheme(ButtonTheme.DEFAULT);

		deleteButton = new Button("Delete Artist")
				.setTheme(ButtonTheme.DANGER)
				.setVisible(false);
	}

	/**
	 * Sets up data binding context for the form.
	 */
	private void setupDataBinding() {
		bindingContext = BindingContext.of(this, MusicArtist.class, true);
	}

	/**
	 * Configures the dialog layout.
	 */
	private void setupLayout() {
		FlexLayout content = new FlexLayout();
		content.setDirection(FlexDirection.COLUMN);
		content.addClassName("dialog-content");

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

		FlexLayout buttonBar = new FlexLayout();
		buttonBar.setDirection(FlexDirection.ROW);
		buttonBar.addClassName("dialog-buttons");

		FlexLayout rightButtons = new FlexLayout();
		rightButtons.setDirection(FlexDirection.ROW);
		rightButtons.addClassName("dialog-buttons-right");
		rightButtons.add(saveButton, cancelButton);

		buttonBar.add(deleteButton, rightButtons);

		content.add(form, buttonBar);
		self.add(content);
	}

	/**
	 * Sets up event handlers for dialog components.
	 */
	private void setupEventHandlers() {
		saveButton.addClickListener(e -> saveArtist());
		cancelButton.addClickListener(e -> self.close());
		deleteButton.addClickListener(e -> deleteArtist());
		self.addCloseListener(e -> resetForm());
	}

	/**
	 * Handles the save operation for both add and edit modes.
	 */
	private void saveArtist() {
		try {
			ValidationResult validationResult = bindingContext.write(artist);

			if (!validationResult.isValid()) {
				return;
			}

			if (currentMode == Mode.ADD) {
				artistService.createArtist(artist);
				Toast.show("Artist '" + artist.getName() + "' added successfully!", Theme.SUCCESS);
			} else {
				artistService.updateArtist(artist);
				Toast.show("Artist '" + artist.getName() + "' updated successfully!", Theme.SUCCESS);
			}

			if (onSaveCallback != null) {
				onSaveCallback.run();
			}

			self.close();

		} catch (Exception ex) {
			String action = currentMode == Mode.ADD ? "saving" : "updating";
			Toast.show("Error " + action + " artist: " + ex.getMessage(), Theme.DANGER);
		}
	}

	/**
	 * Updates the dialog UI based on the current mode (ADD or EDIT).
	 */
	private void updateDialogForMode() {
		if (currentMode == Mode.ADD) {
			title.setText("Add New Artist");
			saveButton.setText("Save Artist");
			deleteButton.setVisible(false);
		} else {
			title.setText("Edit Artist");
			saveButton.setText("Update Artist");
			deleteButton.setVisible(true);
		}
	}

	/**
	 * Handles the delete operation with confirmation dialog.
	 */
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
					
					if (onSaveCallback != null) {
						onSaveCallback.run();
					}
					
					self.close();
					
				} catch (Exception ex) {
					Toast.show("Error deleting artist: " + ex.getMessage(), Theme.DANGER);
				}
			}
		}
	}

	/**
	 * Resets the form fields based on the current mode.
	 */
	private void resetForm() {
		if (currentMode == Mode.ADD) {
			artist = new MusicArtist();
			artist.setIsActive(true);
		}

		bindingContext.read(artist);
	}

	/**
	 * Shows the dialog in ADD mode for creating a new artist.
	 */
	public void showDialog() {
		this.currentMode = Mode.ADD;
		resetForm();
		updateDialogForMode();
		self.open();
	}

	/**
	 * Shows the dialog in EDIT mode for modifying an existing artist.
	 * 
	 * @param existingArtist the artist to edit
	 */
	public void showDialog(MusicArtist existingArtist) {
		this.currentMode = Mode.EDIT;
		this.artist = existingArtist;
		bindingContext.read(artist);
		updateDialogForMode();
		self.open();
	}
}