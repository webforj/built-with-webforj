package com.webforj.builtwithwebforj.ui5.views;

import com.webforj.builtwithwebforj.ui5.components.Ui5Button;
import com.webforj.builtwithwebforj.ui5.components.Ui5DatePicker;
import com.webforj.builtwithwebforj.ui5.components.Ui5Dialog;
import com.webforj.builtwithwebforj.ui5.components.Ui5Field;
import com.webforj.builtwithwebforj.ui5.components.Ui5Input;
import com.webforj.builtwithwebforj.ui5.components.Ui5Label;
import com.webforj.builtwithwebforj.ui5.components.Ui5Select;
import com.webforj.builtwithwebforj.ui5.components.Ui5Switch;
import com.webforj.builtwithwebforj.ui5.components.Ui5Text;
import com.webforj.builtwithwebforj.ui5.components.Ui5Title;
import com.webforj.builtwithwebforj.ui5.customers.Customer;
import com.webforj.builtwithwebforj.ui5.customers.CustomerService;
import com.webforj.builtwithwebforj.ui5.customers.Status;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.binding.annotation.BindingExclude;
import com.webforj.router.NavigationOptions;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.WillEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.WillEnterObserver;
import java.util.List;
import java.util.Optional;

/**
 * Edits one customer, or creates one when the id in the path is {@value #NEW}.
 *
 * <p>
 * The fields are bound to the {@link Customer} properties of the same name and validated with its
 * constraints. That is plain webforJ data binding, and it works with the UI5 controls because they
 * implement the webforJ value and validation contracts.
 * </p>
 */
@Route(value = "customers/:id", outlet = MainLayout.class)
@FrameTitle("Customer")
public class CustomerFormView extends Composite<Div> implements WillEnterObserver {
  /** The id that opens an empty form for a new customer. */
  public static final String NEW = "new";

  private final CustomerService service;
  private final Notifier notifier;
  private final BindingContext<Customer> binding;
  private Customer customer;

  private final Ui5Title heading = new Ui5Title("");
  private final Ui5Input name = new Ui5Input("Name");
  private final Ui5Input email = new Ui5Input("Email");
  private final Ui5Select<Status> status = new Ui5Select<>("Status");
  // Bound by hand in the constructor, because its binding needs a rule of its own.
  @BindingExclude
  private final Ui5DatePicker contractStart = new Ui5DatePicker("Contract start");
  private final Ui5Switch newsletter = new Ui5Switch("Newsletter");
  private final Ui5Button delete = new Ui5Button("Delete", Ui5Button.Design.NEGATIVE);
  private final Ui5Dialog confirm = new Ui5Dialog("Delete customer");
  private final Ui5Text confirmText = new Ui5Text("");

  /**
   * Creates the view.
   *
   * @param service the customer service
   * @param notifier shows the messages
   */
  public CustomerFormView(CustomerService service, Notifier notifier) {
    this.service = service;
    this.notifier = notifier;

    email.setType(Ui5Input.Type.EMAIL);
    status.setItems(List.of(Status.values()), Status::getLabel);
    binding = BindingContext.of(this, Customer.class, true);
    // Text that is no date reads as an empty picker, so without this rule it is saved as no date.
    binding.bind(contractStart, "contractStart")
        .useValidator(date -> !contractStart.hasInvalidText(), "Enter a valid date").add();

    Div form = new Div(labeled(name), labeled(email), labeled(status), labeled(contractStart),
        labeled(newsletter));
    form.addClassName("form");

    Ui5Button save = new Ui5Button("Save", Ui5Button.Design.EMPHASIZED);
    save.onClick(e -> save());
    delete.onClick(e -> askBeforeDelete());
    Ui5Button back = new Ui5Button("Back to customers", Ui5Button.Design.TRANSPARENT);
    back.onClick(e -> showCustomers());

    Div actions = new Div(save, delete, back);
    actions.addClassName("toolbar");

    Ui5Button confirmDelete = new Ui5Button("Yes, delete", Ui5Button.Design.NEGATIVE);
    confirmDelete.onClick(e -> delete());
    Ui5Button cancel = new Ui5Button("Cancel", Ui5Button.Design.TRANSPARENT);
    cancel.onClick(e -> confirm.close());
    confirm.add(confirmText);
    confirm.addToFooter(confirmDelete, cancel);

    getBoundComponent().addClassName("page", "page--narrow").add(heading, form, actions, confirm);
  }

  // Runs before the view is shown. An id that matches no customer leads back to the list.
  @Override
  public void onWillEnter(WillEnterEvent event, ParametersBag parameters) {
    String id = parameters.get("id").orElse(NEW);
    if (NEW.equals(id)) {
      edit(new Customer());
      event.accept();
      return;
    }

    parseId(id).flatMap(service::find).ifPresentOrElse(found -> {
      edit(found);
      event.accept();
    }, () -> {
      event.reject();
      showCustomers();
    });
  }

  private void edit(Customer customer) {
    this.customer = customer;
    boolean isNew = customer.getId() == null;
    heading.setText(isNew ? "New customer" : customer.getName());
    delete.setEnabled(!isNew);
    binding.read(customer);
  }

  private void save() {
    if (binding.write(customer).isValid()) {
      service.save(customer);
      notifier.show("Saved " + customer.getName());
      showCustomers();
    }
  }

  private void askBeforeDelete() {
    confirmText.setText("Delete " + customer.getName() + "? This cannot be undone.");
    confirm.open();
  }

  private void delete() {
    confirm.close();
    service.delete(customer.getId());
    notifier.show("Deleted " + customer.getName());
    showCustomers();
  }

  private static Optional<Long> parseId(String id) {
    try {
      return Optional.of(Long.valueOf(id));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  // The list is rendered from scratch. When the router turns an unknown customer away, the list
  // the user came from is already removed, and a plain navigation to it would change nothing.
  private static void showCustomers() {
    Router.getCurrent().navigate(CustomersView.class,
        new NavigationOptions().setRecreateFrom(CustomersView.class));
  }

  // UI5 controls show no label themselves, so each one gets a UI5 label above it.
  private static Div labeled(Ui5Field<?, ?> field) {
    Div labeled = new Div(new Ui5Label(field.getLabel()).setRequired(field.isRequired()), field);
    labeled.addClassName("form__field");
    return labeled;
  }
}
