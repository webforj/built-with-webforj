package com.webforj.databinding.views;

import com.webforj.component.Composite;
import com.webforj.component.accordion.Accordion;
import com.webforj.component.accordion.AccordionPanel;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Fieldset;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.columnslayout.ColumnsLayout;
import com.webforj.component.layout.columnslayout.ColumnsLayout.Breakpoint;
import com.webforj.component.list.ChoiceBox;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.binding.annotation.UseProperty;
import com.webforj.data.validation.server.ValidationResult;
import com.webforj.databinding.domain.Address;
import com.webforj.databinding.domain.EmergencyContact;
import com.webforj.databinding.domain.Employee;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import java.util.List;

/**
 * Single-page demo of webforJ data binding with nested beans.
 *
 * <p>The form is backed by a single {@link BindingContext} over {@link Employee}, created with
 * {@code BindingContext.of()} (automatic binding). Fields whose names match Employee properties
 * bind by name; fields inside nested beans ({@link Address} and
 * {@link com.webforj.databinding.domain.EmergencyContact}) are mapped with {@code @UseProperty}
 * dotted paths — e.g. {@code "address.street"}. {@code BindingContext.write()} populates the
 * nested beans, creating missing intermediate objects through their no-arg constructors as
 * needed, and Jakarta Validation cascades through {@code @Valid} on the nested fields.
 */
@Route("/")
@FrameTitle("Employee Onboarding")
public class EmployeeFormView extends Composite<Div> {

  private final Div self = getBoundComponent();

  // Employee fields
  private final TextField firstName = new TextField("First name");
  private final TextField lastName = new TextField("Last name");
  private final TextField email = new TextField("Email");
  private final ChoiceBox role = new ChoiceBox("Role");

  // Address fields (nested under Employee.address)
  @UseProperty("address.street")
  private final TextField street = new TextField("Street");

  @UseProperty("address.city")
  private final TextField city = new TextField("City");

  @UseProperty("address.postalCode")
  private final TextField postalCode = new TextField("Postal code");

  @UseProperty("address.country")
  private final ChoiceBox countryBox = new ChoiceBox("Country");

  // EmergencyContact fields (nested under Employee.emergencyContact)
  @UseProperty("emergencyContact.contactName")
  private final TextField contactName = new TextField("Contact name");

  @UseProperty("emergencyContact.relationship")
  private final ChoiceBox relationshipBox = new ChoiceBox("Relationship");

  @UseProperty("emergencyContact.phone")
  private final TextField phone = new TextField("Phone");

  private final Button saveButton = new Button("Save");

  private BindingContext<Employee> context;
  private Employee current = new Employee();

  public EmployeeFormView() {
    populateChoiceBoxes();
    buildLayout();
    setupBindings();
  }

  private void populateChoiceBoxes() {
    role.insert("Engineer", "Designer", "Product Manager", "Operations");
    countryBox.insert("United States", "United Kingdom", "Canada", "Germany", "Australia");
    relationshipBox.insert("Spouse", "Parent", "Sibling", "Friend", "Other");
  }

  private void buildLayout() {
    self.setMaxWidth("720px")
        .setStyle("margin", "var(--dwc-space-xl) auto")
        .setStyle("padding", "0 var(--dwc-space-m) var(--dwc-space-3xl)")
        .setStyle("display", "flex")
        .setStyle("flex-direction", "column")
        .setStyle("gap", "var(--dwc-space-l)");

    saveButton.setTheme(ButtonTheme.PRIMARY)
        .setPrefixComponent(FeatherIcon.CHECK.create())
        .setEnabled(false);
    saveButton.onClick(e -> saveCurrent());

    self.add(
        new H1("Employee Onboarding"),
        new Paragraph(
            "Demonstrates webforJ 26.01 automatic data binding with nested beans. A single "
                + "BindingContext.of() binds top-level Employee fields by name and nested "
                + "Address / EmergencyContact fields via @UseProperty dotted paths."),
        identityFieldset(),
        addressFieldset(),
        contactFieldset(),
        saveButton);
  }

  private Fieldset identityFieldset() {
    ColumnsLayout grid = new ColumnsLayout(firstName, lastName, email, role);
    applyBreakpoints(grid);
    grid.setSpan(email, "wide", 2);
    grid.setSpan(role, "wide", 2);

    return fieldset("Employee", grid);
  }

  private Fieldset addressFieldset() {
    ColumnsLayout grid = new ColumnsLayout(street, city, postalCode, countryBox);
    applyBreakpoints(grid);
    grid.setSpan(street, "wide", 2);
    grid.setSpan(countryBox, "wide", 2);

    return fieldset("Address", grid);
  }

  private Fieldset contactFieldset() {
    ColumnsLayout grid = new ColumnsLayout(contactName, relationshipBox, phone);
    applyBreakpoints(grid);
    grid.setSpan(phone, "wide", 2);

    return fieldset("Emergency contact", grid);
  }

  /**
   * Wraps a {@link ColumnsLayout} in a {@link Fieldset} with extra interior padding so labels
   * have room to breathe — the bare HTML fieldset is tight by default.
   */
  private Fieldset fieldset(String legend, ColumnsLayout grid) {
    Fieldset fs = new Fieldset(legend);
    fs.setStyle("padding", "var(--dwc-space-l) var(--dwc-space-l) var(--dwc-space-xl)");
    fs.setStyle("border-radius", "var(--dwc-border-radius)");
    fs.add(grid);

    return fs;
  }

  private void applyBreakpoints(ColumnsLayout grid) {
    grid.setBreakpoints(List.of(
        new Breakpoint("default", 0, 1),
        new Breakpoint("wide", "40em", 2)));
    grid.setSpacing("var(--dwc-space-m)");
  }

  private void setupBindings() {
    // Auto binding: fields whose names match Employee properties bind by name; the
    // rest are mapped through @UseProperty, including nested dotted paths.
    context = BindingContext.of(this, Employee.class, true);

    context.onValidate(e -> saveButton.setEnabled(e.isValid()));
    context.read(current);
  }

  private void saveCurrent() {
    ValidationResult result = context.write(current);
    if (!result.isValid()) {
      return;
    }

    openSavedEmployeeDialog(current);

    resetForm();
  }

  private void resetForm() {
    current = new Employee();
    context.read(current);
    // ChoiceBox doesn't visually clear from an empty string; explicitly deselect on reset.
    role.deselect();
    countryBox.deselect();
    relationshipBox.deselect();
    saveButton.setEnabled(false);
  }

  /**
   * Opens a {@link Dialog} containing an {@link Accordion} with one panel for each of the saved
   * employee's three sections — top-level Employee, nested Address, and nested EmergencyContact.
   */
  private void openSavedEmployeeDialog(Employee e) {
    Dialog dialog = new Dialog();
    dialog.setMaxWidth("560px");
    dialog.setAutoFocus(true);

    Div title = new Div("Saved: " + e.fullName());
    title.setStyle("font-size", "var(--dwc-font-size-l)");
    title.setStyle("font-weight", "var(--dwc-font-weight-semibold)");
    dialog.addToHeader(title);

    Accordion accordion = new Accordion();
    accordion.setMultiple(false);
    accordion.add(employeePanel(e));
    accordion.add(addressPanel(e.getAddress()));
    accordion.add(contactPanel(e.getEmergencyContact()));
    dialog.addToContent(accordion);

    Button close = new Button("Close");
    close.setTheme(ButtonTheme.PRIMARY);
    close.onClick(ev -> dialog.close());

    Div footerWrap = new Div();
    footerWrap.setStyle("padding", "var(--dwc-space-m)");
    footerWrap.setStyle("width", "100%");
    footerWrap.setStyle("box-sizing", "border-box");
    footerWrap.add(close);
    dialog.addToFooter(footerWrap);
    dialog.onClose(ev -> self.remove(dialog));

    self.add(dialog);
    dialog.open();
  }

  private AccordionPanel employeePanel(Employee e) {
    AccordionPanel panel = new AccordionPanel("Employee");
    panel.add(sectionBody(
        detailRow("First name", e.getFirstName()),
        detailRow("Last name", e.getLastName()),
        detailRow("Email", e.getEmail()),
        detailRow("Role", e.getRole())));

    return panel;
  }

  private AccordionPanel addressPanel(Address a) {
    AccordionPanel panel = new AccordionPanel("Address");
    panel.add(sectionBody(
        detailRow("Street", a.getStreet()),
        detailRow("City", a.getCity()),
        detailRow("Postal code", a.getPostalCode()),
        detailRow("Country", a.getCountry())));

    return panel;
  }

  private AccordionPanel contactPanel(EmergencyContact c) {
    AccordionPanel panel = new AccordionPanel("Emergency contact");
    panel.add(sectionBody(
        detailRow("Contact name", c.getContactName()),
        detailRow("Relationship", c.getRelationship()),
        detailRow("Phone", c.getPhone())));

    return panel;
  }

  private Div sectionBody(Div... rows) {
    Div body = new Div();
    body.setStyle("display", "flex");
    body.setStyle("flex-direction", "column");
    body.setStyle("gap", "var(--dwc-space-s)");
    body.setStyle("padding", "var(--dwc-space-m) var(--dwc-space-s)");
    body.add(rows);

    return body;
  }

  private Div detailRow(String label, String value) {
    Div row = new Div();
    row.setStyle("display", "flex");
    row.setStyle("flex-direction", "column");
    row.setStyle("gap", "var(--dwc-space-3xs)");

    Div labelEl = new Div(label);
    labelEl.setStyle("font-size", "var(--dwc-font-size-xs)");
    labelEl.setStyle("text-transform", "uppercase");
    labelEl.setStyle("letter-spacing", "0.04em");
    labelEl.setStyle("color", "var(--dwc-color-gray-text)");

    Div valueEl = new Div(value);
    valueEl.setStyle("font-size", "var(--dwc-font-size-m)");

    row.add(labelEl, valueEl);

    return row;
  }
}
