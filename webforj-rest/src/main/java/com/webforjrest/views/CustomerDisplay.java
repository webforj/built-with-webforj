package com.webforjrest.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.navigator.Navigator;
import com.webforj.component.table.Table;
import com.webforj.data.repository.CollectionRepository;
import com.webforjrest.service.RestClientService;
import com.webforjrest.entity.Customer;

import java.util.List;

/**
 * View for displaying customer data from the REST API.
 * Demonstrates CRUD operations with Spring Boot backend integration.
 */
public class CustomerDisplay extends Composite<FlexLayout> {

  private final RestClientService customerService;

  private FlexLayout container = getBoundComponent();
  private FlexLayout header;
  private FlexLayout tableContainer;

  private H1 pageTitle;
  private Paragraph description;
  private Anchor docsLink;
  private Table<Customer> customerTable;
  private Navigator navigator;

  public CustomerDisplay(RestClientService customerService) {
    this.customerService = customerService;

    initializeComponents();
    setupLayout();
    loadData();
  }

  private void initializeComponents() {
    pageTitle = new H1("Customer Management (In Memory)");
    description = new Paragraph();

    description.setHtml(/*html */ """
        This example demonstrates how to use webforJ with a Spring Boot REST API backend. 
        Here, we make a call to the API to fetch all customer data from our backend endpoints collect that data into a List, 
        and then create a <a href='https://docs.webforj.com/docs/advanced/repository/overview#collection-repository' target='blank'>CollectionRepository</a> from that list. <br /><br />
        
        We use <a href='https://javadoc.io/doc/com.webforj/webforj-table/latest/com/webforj/component/table/Table.html' target='blank'>setRepository()</a> to assign this 
        repository to the <a href='https://docs.webforj.com/docs/components/table/overview' target='blank'>Table</a>, and add a <a href='https://docs.webforj.com/docs/components/navigator' target='blank'>Navigator</a> component configured for 15 items per page.
        While all data is loaded into memory at once, the Table and Navigator provide paginated viewing.
        """
    );

    docsLink = new Anchor("https://docs.webforj.com/docs/integrations/spring/spring-boot",
        "Learn more about Spring Boot integration with webforJ →");
    docsLink.setTarget("blank");
    docsLink.setStyle("color", "var(--dwc-color-primary)");
    docsLink.setStyle("text-decoration", "none");
    docsLink.setStyle("font-weight", "500");

    customerTable = new Table<>();
    setupTableColumns();
  }

  private void setupLayout() {
    container.setDirection(FlexDirection.COLUMN);

    header = new FlexLayout();
    header.setDirection(FlexDirection.COLUMN);
    header.setStyle("margin-bottom", "2rem");
    header.add(pageTitle, description, docsLink);

    tableContainer = new FlexLayout();
    tableContainer.setDirection(FlexDirection.COLUMN);

    container.add(header, tableContainer);
  }

  private void setupTableColumns() {
    customerTable.addColumn("ID", Customer::getId).setMinWidth(80);
    customerTable.addColumn("Name", Customer::getName);
    customerTable.addColumn("Email", Customer::getEmail);
    customerTable.addColumn("Company", Customer::getCompany);
    customerTable.addColumn("Phone", Customer::getPhone);

    customerTable.addClassName("customers-table");
    customerTable.setRowHeight(45);
    customerTable.setHeight("60dvh");
  }

  private void loadData() {
    try {
      // Fetch all customers from the REST API
      List<Customer> customers = customerService.getAllCustomers();

      // Create a CollectionRepository from the list (all data in memory)
      CollectionRepository<Customer> repository = new CollectionRepository<>(customers);

      // Set the repository on the table
      customerTable.setRepository(repository);

      // Create paginator with 15 items per page
      navigator = new Navigator(repository, 15);

      // Add table and navigator to the container
      tableContainer.add(customerTable, navigator);
    } catch (Exception e) {
      System.err.println("Error loading customer data: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
