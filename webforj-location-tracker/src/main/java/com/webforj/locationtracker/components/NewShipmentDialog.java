package com.webforj.locationtracker.components;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListItem;
import com.webforj.locationtracker.model.City;

import java.util.function.BiConsumer;

public class NewShipmentDialog extends Composite<Dialog> {

  private final Dialog self = getBoundComponent();
  private final TextField consigneeField = new TextField();
  private final ChoiceBox destinationChoice = new ChoiceBox();
  private final Button saveBtn = new Button("Create");
  private final Button cancelBtn = new Button("Cancel");
  private BiConsumer<String, City> onSave = (n, c) -> {};

  public NewShipmentDialog() {
    self.addClassName("new-shipment-dialog");
    self.setAutoFocus(true);

    self.addToHeader(new H2("Create shipment"));

    consigneeField.setLabel("Consignee");
    consigneeField.setPlaceholder("e.g. Costa Textiles SL");
    consigneeField.setRequired(true);

    destinationChoice.setLabel("Destination");
    for (City c : City.values()) {
      destinationChoice.add(new ListItem(c, c.getLabel() + ", " + c.getCountry()));
    }
    destinationChoice.selectIndex(0);

    Paragraph hint = new Paragraph(
        "The new shipment arrives with a NEW chip and a fresh tracking ID — watch the badges in "
            + "the header and the browser tab tick up as soon as you hit Create.");
    hint.addClassName("new-shipment-dialog__hint");

    FlexLayout content = FlexLayout.create(consigneeField, destinationChoice, hint)
        .vertical().build()
        .setSpacing("var(--dwc-space-m)")
        .setMinWidth("320px");
    self.add(content);

    saveBtn.setTheme(ButtonTheme.PRIMARY);
    saveBtn.onClick(e -> handleSave());
    cancelBtn.setTheme(ButtonTheme.OUTLINED_GRAY);
    cancelBtn.onClick(e -> close());

    FlexLayout footer = FlexLayout.create(cancelBtn, saveBtn)
        .horizontal().justify().end().build()
        .setSpacing("var(--dwc-space-s)");
    self.addToFooter(footer);
  }

  public NewShipmentDialog onSave(BiConsumer<String, City> callback) {
    this.onSave = callback;
    return this;
  }

  public void open() {
    consigneeField.setValue("");
    consigneeField.setInvalid(false);
    destinationChoice.selectIndex(0);
    self.open();
    consigneeField.focus();
  }

  public void close() {
    self.close();
  }

  private void handleSave() {
    String consignee = consigneeField.getValue() == null ? "" : consigneeField.getValue().trim();
    if (consignee.isEmpty()) {
      consigneeField.setInvalid(true).setInvalidMessage("Consignee name is required");
      consigneeField.focus();
      return;
    }
    // Falling back to a default city here would quietly ship the goods elsewhere.
    ListItem picked = destinationChoice.getSelectedItem();
    if (picked == null) {
      destinationChoice.focus();
      return;
    }

    onSave.accept(consignee, (City) picked.getKey());
    close();
  }
}
