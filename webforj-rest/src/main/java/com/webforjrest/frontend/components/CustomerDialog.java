package com.webforjrest.frontend.components;

import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H2;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.optiondialog.ConfirmDialog;
import com.webforj.component.toast.Toast;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;
import com.webforjrest.frontend.data.RestClientService;
import com.webforjrest.frontend.models.CustomerModel;

public class CustomerDialog extends Composite<Dialog> {

  public enum Mode {
    ADD, EDIT
  }

  private Mode currentMode = Mode.ADD;
  private Dialog self = getBoundComponent();

  private final RestClientService customerService;
  private final Runnable onSaveCallback;

  private BindingContext<CustomerModel> bindingContext;
  private CustomerModel customer;

  private H2 title;
  private TextField name;
  private TextField email;
  private TextField company;
  private TextField phone;

  private FlexLayout buttonBar;
  private Button saveButton;
  private Button cancelButton;
  private Button deleteButton;

  public CustomerDialog(RestClientService customerService, Runnable onSaveCallback) {
    super();
    this.customerService = customerService;
    this.onSaveCallback = onSaveCallback;

    initializeDialog();
    initializeComponents();
    setupDataBinding();
    setupLayout();
    setupEventHandlers();
  }

  private void initializeDialog() {
    title = new H2("Add New Customer");
    self.addToHeader(title);
    self.addClassName("customer-dialog");
    self.setCancelOnOutsideClick(false);
    self.setBlurred(true);
  }

  private void initializeComponents() {
    name = new TextField()
        .setLabel("Name")
        .setPlaceholder("Enter customer name");

    email = new TextField()
        .setLabel("Email")
        .setPlaceholder("customer@example.com");

    company = new TextField()
        .setLabel("Company")
        .setPlaceholder("Company name");

    phone = new TextField()
        .setLabel("Phone")
        .setPlaceholder("555-0100");

    saveButton = new Button("Save Customer")
        .setTheme(ButtonTheme.PRIMARY);

    cancelButton = new Button("Cancel");

    deleteButton = new Button("Delete Customer")
        .setTheme(ButtonTheme.DANGER);
  }

  private void setupDataBinding() {
    bindingContext = BindingContext.of(this, CustomerModel.class, true);
  }

  private void setupLayout() {
    FlexLayout content = new FlexLayout();
    content.setDirection(FlexDirection.COLUMN);

    FlexLayout form = new FlexLayout();
    form.setDirection(FlexDirection.COLUMN);
    form.add(name, email, company, phone);

    buttonBar = new FlexLayout();

    FlexLayout rightButtons = new FlexLayout();
    rightButtons.add(saveButton, cancelButton);

    buttonBar.add(deleteButton, rightButtons);

    content.add(form, buttonBar);
    self.add(content);
  }

  private void setupEventHandlers() {
    saveButton.addClickListener(e -> saveCustomer());
    cancelButton.addClickListener(e -> self.close());
    deleteButton.addClickListener(e -> deleteCustomer());
    self.addCloseListener(e -> resetForm());
  }

  private void saveCustomer() {
    try {
      ValidationResult validationResult = bindingContext.write(customer);
      if (!validationResult.isValid()) {
        return;
      }
      if (currentMode == Mode.ADD) {
        customerService.createCustomer(customer);
        Toast.show("Customer '" + customer.getName() + "' added successfully!", Theme.SUCCESS);
      } else {
        customerService.updateCustomer(customer.getId(), customer);
        Toast.show("Customer '" + customer.getName() + "' updated successfully!", Theme.SUCCESS);
      }
      if (onSaveCallback != null) {
        onSaveCallback.run();
      }
      self.close();
    } catch (Exception ex) {
      String action = currentMode == Mode.ADD ? "saving" : "updating";
      Toast.show("Error " + action + " customer: " + ex.getMessage(), Theme.DANGER);
    }
  }

  private void updateDialogForMode() {
    if (currentMode == Mode.ADD) {
      title.setText("Add New Customer");
      saveButton.setText("Save Customer");
      deleteButton.setVisible(false);
      buttonBar.setJustifyContent(FlexJustifyContent.END);
    } else {
      title.setText("Edit Customer");
      saveButton.setText("Update Customer");
      deleteButton.setVisible(true);
      buttonBar.setJustifyContent(FlexJustifyContent.BETWEEN);
    }
  }

  private void deleteCustomer() {
    if (currentMode == Mode.EDIT && customer != null) {
      ConfirmDialog dialog = new ConfirmDialog(
          "Are you sure you want to delete '" + customer.getName() + "'? This action cannot be undone.",
          "Delete Customer",
          ConfirmDialog.OptionType.YES_NO,
          ConfirmDialog.MessageType.QUESTION);

      dialog.setTheme(Theme.DANGER);
      dialog.setButtonTheme(ConfirmDialog.Button.FIRST, ButtonTheme.DANGER);
      dialog.setButtonTheme(ConfirmDialog.Button.SECOND, ButtonTheme.OUTLINED_GRAY);

      ConfirmDialog.Result result = dialog.show();

      if (result == ConfirmDialog.Result.YES) {
        try {
          customerService.deleteCustomer(customer.getId());
          Toast.show("Customer '" + customer.getName() + "' deleted successfully!", Theme.SUCCESS);

          if (onSaveCallback != null) {
            onSaveCallback.run();
          }

          self.close();

        } catch (Exception ex) {
          Toast.show("Error deleting customer: " + ex.getMessage(), Theme.DANGER);
        }
      }
    }
  }

  private void resetForm() {
    if (currentMode == Mode.ADD) {
      customer = new CustomerModel();
    }
    bindingContext.read(customer);
  }

  public void showDialog() {
    this.currentMode = Mode.ADD;
    resetForm();
    updateDialogForMode();
    self.open();
  }

  public void showDialog(CustomerModel existingCustomer) {
    this.currentMode = Mode.EDIT;
    this.customer = existingCustomer;
    bindingContext.read(customer);
    updateDialogForMode();
    self.open();
  }
}
