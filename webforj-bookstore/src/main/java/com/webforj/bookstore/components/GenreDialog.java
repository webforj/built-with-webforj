package com.webforj.bookstore.components;

import com.webforj.bookstore.domain.Genre;
import com.webforj.component.Composite;

import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.ColorField;
import com.webforj.component.field.TextField;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexAlignment;

import com.webforj.component.html.elements.H2;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import java.awt.Color;

import java.util.function.Consumer;

/**
 * A dialog for creating a new genre.
 */
public class GenreDialog extends Composite<Dialog> {

    private final Dialog self = getBoundComponent();
    private final Consumer<Genre> onSave;

    private TextField nameField;
    private ColorField colorField;

    public GenreDialog(Consumer<Genre> onSave) {
        this.onSave = onSave;

        configureDialog();
        createContent();
    }

    private void configureDialog() {
        FlexLayout header = new FlexLayout();
        header.setDirection(FlexDirection.ROW);
        header.setSpacing("var(--dwc-space-s)");
        header.setStyle("align-items", "center");

        FlexLayout titleLayout = new FlexLayout();
        titleLayout.setDirection(FlexDirection.ROW);
        titleLayout.setSpacing("var(--dwc-space-s)");
        titleLayout.setAlignment(FlexAlignment.BASELINE);
        titleLayout.add(FeatherIcon.FOLDER.create(), new H2("Add New Genre"));

        header.add(titleLayout);

        self.addToHeader(header);
        self.setCancelOnOutsideClick(true);
        self.setBlurred(true);
        self.setStyle("max-width", "400px");
    }

    private void createContent() {
        FlexLayout content = new FlexLayout();
        content.setDirection(FlexDirection.COLUMN);
        content.setSpacing("var(--dwc-space-m)");
        content.setStyle("padding", "var(--dwc-space-m)");

        nameField = new TextField("Genre Name");
        nameField.setPlaceholder("e.g. Science Fiction");
        nameField.onModify(e -> nameField.setInvalid(false)); // Clear validation on input

        // Compact color picker as prefix component
        colorField = new ColorField();
        colorField.setWidth("30px");
        colorField.setHeight("30px");
        colorField.addClassName("genre-color-picker");
        colorField.setValue(Color.BLUE);

        nameField.setPrefixComponent(colorField);
        content.add(nameField);

        // Footer buttons in horizontal layout
        FlexLayout footer = new FlexLayout();
        footer.setDirection(FlexDirection.ROW);
        footer.setJustifyContent(FlexJustifyContent.END);
        footer.setSpacing("var(--dwc-space-s)");

        Button saveButton = new Button("Add", ButtonTheme.PRIMARY, e -> save());
        saveButton.setPrefixComponent(FeatherIcon.PLUS.create());
        Button cancelButton = new Button("Cancel", e -> self.close());

        footer.add(saveButton, cancelButton);

        self.add(content);
        self.addToFooter(footer);
    }

    public void open() {
        nameField.setText("");
        colorField.setValue(Color.BLUE);
        self.open();
    }

    private void save() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            nameField.setInvalid(true);
            nameField.setInvalidMessage("can not be empty.");
            return;
        }
        nameField.setInvalid(false);

        Color c = colorField.getValue();
        // Convert to hex string
        String color = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());

        Genre genre = new Genre();
        genre.setName(name);
        genre.setColor(color);

        onSave.accept(genre);
        self.close();
    }
}
