package com.webforjrest.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.navigator.Navigator;
import com.webforj.component.table.Table;
import com.webforjrest.service.CustomerDelegatingRepository;
import com.webforjrest.entity.Customer;

/**
 * View for displaying customers from REST API with DelegatingRepository.
 * Demonstrates DelegatingRepository pattern with pagination.
 */
public class CustomerDelegatingDisplay extends Composite<FlexLayout> {

  private final CustomerDelegatingRepository customerRepository;

  private FlexLayout container = getBoundComponent();
  private FlexLayout header;
  private FlexLayout tableContainer;

  private H1 pageTitle;
  private Paragraph description;
  private Anchor docsLink;
  private Table<Customer> customersTable;
  private Navigator navigator;

  public CustomerDelegatingDisplay(CustomerDelegatingRepository customerRepository) {
    this.customerRepository = customerRepository;

    initializeComponents();
    setupLayout();
  }

  private void initializeComponents() {
    pageTitle = new H1("Customer Management (Lazy Loading)");
    description = new Paragraph();
    description.setHtml(/* html */ """
       Similar to the previous example, this display demonstrates using webforJ with REST APIs, but this time using the <a href='https://docs.webforj.com/docs/advanced/repository/delegating-repository#how-delegatingrepository-works' target='blank'>DelegatingRepository</a> pattern to lazily load the data.
       Instead of loading all data at once, we create a CustomerDelegatingRepository that handles fetching customers from
       our Spring Boot backend (/api/customers) on demand.
      """
    );

    docsLink = new Anchor("https://docs.webforj.com/docs/advanced/repository/delegating-repository",
        "Learn more about how to manage and query collections of entities →");
    docsLink.setTarget("blank");
    docsLink.setStyle("color", "var(--dwc-color-primary)");
    docsLink.setStyle("text-decoration", "none");
    docsLink.setStyle("font-weight", "500");

    customersTable = new Table<>();
    customersTable.setRepository(customerRepository);
    setupTableColumns();

    // Create paginator with 15 items per page
    navigator = new Navigator(customerRepository, 15);
    navigator.setLayout(Navigator.Layout.PAGES);
  }

  private void setupLayout() {
    container.setDirection(FlexDirection.COLUMN);

    header = new FlexLayout();
    header.setDirection(FlexDirection.COLUMN);
    header.setStyle("margin-bottom", "2rem");
    header.add(pageTitle, description, docsLink);

    tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);
    tableContainer.add(customersTable, navigator);

    container.add(header, tableContainer);
  }

  private void setupTableColumns() {
    customersTable.addColumn("ID", Customer::getId).setMinWidth(80);
    customersTable.addColumn("Name", Customer::getName);
    customersTable.addColumn("Email", Customer::getEmail);
    customersTable.addColumn("Company", Customer::getCompany);
    customersTable.addColumn("Phone", Customer::getPhone);

    customersTable.addClassName("customers-table");
    customersTable.setRowHeight(40);
    customersTable.setHeight("55dvh");
  }
}
