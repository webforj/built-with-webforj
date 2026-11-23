package com.webforj.builtwithwebforj.ui5.views;

import com.webforj.Debouncer;
import com.webforj.builtwithwebforj.ui5.components.Ui5Button;
import com.webforj.builtwithwebforj.ui5.components.Ui5Input;
import com.webforj.builtwithwebforj.ui5.components.Ui5Table;
import com.webforj.builtwithwebforj.ui5.components.Ui5Title;
import com.webforj.builtwithwebforj.ui5.customers.Customer;
import com.webforj.builtwithwebforj.ui5.customers.CustomerService;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.annotation.RouteAlias;
import com.webforj.router.history.ParametersBag;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Lists the customers in a UI5 table, with a search field that filters while the user types.
 */
@Route(value = "/", outlet = MainLayout.class)
@RouteAlias("customers")
@FrameTitle("Customers")
public class CustomersView extends Composite<Div> {
  private static final DateTimeFormatter DATE =
      DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

  private final Ui5Input search = new Ui5Input("Search customers");
  private final Ui5Table<Customer> table = new Ui5Table<>();
  // Waits until typing pauses, so one search runs per term and not one per keystroke.
  private final Debouncer searchDebouncer = new Debouncer(0.3f);

  /**
   * Creates the view.
   *
   * @param service the customer service
   */
  public CustomersView(CustomerService service) {
    search.setType(Ui5Input.Type.SEARCH).setPlaceholder("Search by name, email or status")
        .setShowClearIcon(true).addClassName("toolbar__search");
    search.onValueChange(
        e -> searchDebouncer.run(() -> table.setItems(service.search(search.getValue()))));

    Ui5Button create = new Ui5Button("New customer", Ui5Button.Design.EMPHASIZED);
    create.onClick(e -> open(CustomerFormView.NEW));

    Div toolbar = new Div(search, create);
    toolbar.addClassName("toolbar");

    table.addColumn("Name", Customer::getName);
    table.addColumn("Email", Customer::getEmail);
    table.addColumn("Status", customer -> customer.getStatus().getLabel());
    table.addColumn("Contract start", customer -> customer.getContractStart() == null ? ""
        : DATE.format(customer.getContractStart()));
    table.setNoDataText("No customers match your search");
    table.setItems(service.search(""));
    table.onItemClick(customer -> open(String.valueOf(customer.getId())));

    getBoundComponent().addClassName("page").add(new Ui5Title("Customers"), toolbar, table);
  }

  @Override
  protected void onDidDestroy() {
    searchDebouncer.cancel();
  }

  private static void open(String id) {
    Router.getCurrent().navigate(CustomerFormView.class, ParametersBag.of("id=" + id));
  }
}
