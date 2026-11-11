package com.webforj.builtwithwebforj.crud.components;

import com.webforj.builtwithwebforj.crud.entity.MusicArtist;
import com.webforj.builtwithwebforj.crud.service.MusicArtistService;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.NumberField;
import com.webforj.component.field.TextArea;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H3;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.optiondialog.ConfirmDialog;
import com.webforj.component.optioninput.CheckBox;
import com.webforj.component.toast.Toast;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.binding.annotation.UseTransformer;
import com.webforj.data.validation.server.ValidationResult;

/**
 * Dialog that adds a new artist or edits and deletes an existing one. The fields are bound to
 * the {@link MusicArtist} properties of the same name and validated with its constraints.
 */
public class ArtistDialog extends Composite<Dialog> {

  /**
   * Whether the dialog creates an artist or edits one.
   */
  public enum Mode {
    ADD, EDIT
  }

  private final Dialog self = getBoundComponent();
  private final MusicArtistService artistService;
  private final Runnable onSaveCallback;
  private final BindingContext<MusicArtist> bindingContext;

  private Mode currentMode = Mode.ADD;
  private MusicArtist artist;

  private final H3 title = new H3("Add New Artist");
  private final TextField name = new TextField("Artist Name");
  private final TextField genre = new TextField("Genre");
  private final TextField country = new TextField("Country");
  @UseTransformer(YearTransformer.class)
  private final NumberField yearFormed = new NumberField("Year Formed");
  private final CheckBox isActive = new CheckBox("Currently Active");
  private final TextArea biography = new TextArea("Biography");

  private final Button saveButton = new Button("Save Artist", ButtonTheme.PRIMARY);
  private final Button cancelButton = new Button("Cancel");
  private final Button deleteButton = new Button("Delete Artist", ButtonTheme.OUTLINED_DANGER);

  /**
   * Creates the dialog.
   *
   * @param artistService the service that stores the artists
   * @param onSaveCallback runs after an artist was saved or deleted
   */
  public ArtistDialog(MusicArtistService artistService, Runnable onSaveCallback) {
    this.artistService = artistService;
    this.onSaveCallback = onSaveCallback;

    name.setPlaceholder("Enter artist or band name");
    genre.setPlaceholder("e.g., Rock, Pop, Jazz");
    country.setPlaceholder("Country of origin");
    yearFormed.setPlaceholder("Year artist/band was formed");
    yearFormed.setStep(1d);
    biography.setPlaceholder("Brief biography (optional)");
    biography.setRows(3);

    FlexLayout origin = new FlexLayout(genre, country);
    origin.addClassName("artist-dialog__row");
    origin.setWrap(FlexWrap.WRAP).setSpacing("var(--dwc-space-m)");
    FlexLayout form = new FlexLayout(name, origin, yearFormed, isActive, biography);
    form.setDirection(FlexDirection.COLUMN).setSpacing("var(--dwc-space-m)");
    form.addClassName("artist-dialog__form");

    FlexLayout actions = new FlexLayout(cancelButton, saveButton);
    actions.addClassName("artist-dialog__actions");
    actions.setSpacing("var(--dwc-space-s)");
    FlexLayout footer = new FlexLayout(deleteButton, actions);
    footer.addClassName("artist-dialog__footer");
    footer.setSpacing("var(--dwc-space-s)");

    self.addToHeader(title);
    self.add(form);
    self.addToFooter(footer);
    self.addClassName("artist-dialog");
    self.setCancelOnOutsideClick(false);
    self.setBlurred(true);

    bindingContext = BindingContext.of(this, MusicArtist.class, true);

    saveButton.onClick(e -> saveArtist());
    cancelButton.onClick(e -> self.close());
    deleteButton.onClick(e -> deleteArtist());
    self.onClose(e -> resetForm());
  }

  /**
   * Opens the dialog to create a new artist.
   */
  public void showDialog() {
    currentMode = Mode.ADD;
    resetForm();
    updateDialogForMode();
    self.open();
  }

  /**
   * Opens the dialog to edit an existing artist.
   *
   * @param existingArtist the artist to edit
   */
  public void showDialog(MusicArtist existingArtist) {
    currentMode = Mode.EDIT;
    artist = existingArtist;
    bindingContext.read(artist);
    updateDialogForMode();
    self.open();
  }

  private void saveArtist() {
    boolean adding = currentMode == Mode.ADD;
    try {
      ValidationResult result = bindingContext.write(artist);
      if (!result.isValid()) {
        return;
      }

      if (adding) {
        artistService.createArtist(artist);
      } else {
        artistService.updateArtist(artist);
      }
    } catch (Exception ex) {
      Toast.show("Error " + (adding ? "saving" : "updating") + " artist: " + ex.getMessage(),
          Theme.DANGER);
      return;
    }

    // The artist is stored at this point, so a failing refresh is not reported as a failed save.
    Toast.show("Artist '" + artist.getName() + "' " + (adding ? "added" : "updated")
        + " successfully!", Theme.SUCCESS);
    self.close();
    onSaveCallback.run();
  }

  private void deleteArtist() {
    if (currentMode != Mode.EDIT || artist == null) {
      return;
    }

    ConfirmDialog dialog = new ConfirmDialog(
        "Are you sure you want to delete '" + artist.getName()
            + "'? This action cannot be undone.",
        "Delete Artist", ConfirmDialog.OptionType.YES_NO, ConfirmDialog.MessageType.QUESTION);
    dialog.setTheme(Theme.DANGER);
    dialog.setButtonTheme(ConfirmDialog.Button.FIRST, ButtonTheme.DANGER);
    dialog.setButtonTheme(ConfirmDialog.Button.SECOND, ButtonTheme.OUTLINED_GRAY);

    if (dialog.show() != ConfirmDialog.Result.YES) {
      return;
    }

    try {
      artistService.deleteArtist(artist.getId());
    } catch (Exception ex) {
      Toast.show("Error deleting artist: " + ex.getMessage(), Theme.DANGER);
      return;
    }

    Toast.show("Artist '" + artist.getName() + "' deleted successfully!", Theme.SUCCESS);
    self.close();
    onSaveCallback.run();
  }

  private void updateDialogForMode() {
    boolean editing = currentMode == Mode.EDIT;
    title.setText(editing ? "Edit Artist" : "Add New Artist");
    saveButton.setText(editing ? "Update Artist" : "Save Artist");
    deleteButton.setVisible(editing);
  }

  private void resetForm() {
    if (currentMode == Mode.ADD) {
      artist = new MusicArtist();
      artist.setIsActive(true);
    }

    bindingContext.read(artist);
  }
}
