package com.webforj.locationtracker.components;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListItem;
import com.webforj.locationtracker.model.City;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class AddFriendDialog extends Composite<Dialog> {

  private final Dialog self = getBoundComponent();
  private final TextField nameField = new TextField();
  private final ChoiceBox cityChoice = new ChoiceBox();
  private final Button saveBtn = new Button("Add");
  private final Button cancelBtn = new Button("Cancel");
  private BiConsumer<String, City> onSave = (n, c) -> {};

  public AddFriendDialog() {
    self.addClassName("add-friend-dialog");
    self.setAutoFocus(true);

    self.addToHeader(new H2("Add a friend"));

    nameField.setLabel("Name");
    nameField.setPlaceholder("e.g. Sofia Reyes");
    nameField.setRequired(true);

    cityChoice.setLabel("City");
    for (City c : City.values()) {
      cityChoice.add(new ListItem(c.name(), c.getFlag() + "  " + c.getLabel() + ", " + c.getCountry()));
    }
    cityChoice.selectIndex(0);

    Paragraph hint = new Paragraph(
        "Your new friend arrives with a NEW chip — watch the badges in the header and the browser tab tick up.");
    hint.addClassName("add-friend-dialog__hint");

    Div content = new Div(nameField, cityChoice, hint).addClassName("add-friend-dialog__content");
    self.add(content);

    saveBtn.setTheme(ButtonTheme.PRIMARY);
    saveBtn.onClick(e -> handleSave());
    cancelBtn.setTheme(ButtonTheme.OUTLINED_GRAY);
    cancelBtn.onClick(e -> close());

    Div footer = new Div(cancelBtn, saveBtn).addClassName("add-friend-dialog__footer");
    self.addToFooter(footer);
  }

  public AddFriendDialog onSave(BiConsumer<String, City> callback) {
    this.onSave = callback;
    return this;
  }

  public void open() {
    nameField.setValue("");
    nameField.setInvalid(false);
    cityChoice.selectIndex(0);
    self.open();
    nameField.focus();
  }

  public void close() {
    self.close();
  }

  private void handleSave() {
    String name = nameField.getValue() == null ? "" : nameField.getValue().trim();
    if (name.isEmpty()) {
      nameField.setInvalid(true).setInvalidMessage("Give them a name");
      nameField.focus();
      return;
    }
    ListItem picked = cityChoice.getSelectedItem();
    City city = picked == null ? City.PARIS : Arrays.stream(City.values())
        .filter(c -> c.name().equals(picked.getKey()))
        .findFirst().orElse(City.PARIS);

    onSave.accept(name, city);
    close();
  }

  @SuppressWarnings("unused")
  private void addFooterChild(Component c) {
    // reserved for future footer additions
  }
}
