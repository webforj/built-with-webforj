package com.webforjrest.frontend.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.table.Table;
import com.webforjrest.frontend.data.RestClientService;
import com.webforjrest.frontend.models.CustomerModel;

import java.util.List;

/**
 * View for displaying customer data from the REST API.
 * Demonstrates CRUD operations with Spring Boot backend integration.
 */
public class CustomerView extends Composite<FlexLayout> {

  private final RestClientService customerService;

  private FlexLayout container = getBoundComponent();
  private FlexLayout header;
  private FlexLayout tableContainer;

  private H1 pageTitle;
  private Paragraph description;
  private Anchor docsLink;
  private Table<CustomerModel> customerTable;

  public CustomerView(RestClientService customerService) {
    this.customerService = customerService;

    initializeComponents();
    setupLayout();
    loadData();
  }

  private void initializeComponents() {
    pageTitle = new H1("Customer Management");
    description = new Paragraph(
        "This example demonstrates how to use webforJ with a Spring Boot REST API backend. " +
        "Here, we simply make a call to the RestClientService to fetch customer data from our backend endpoints, " +
        "collect that data into a List<CustomerModel>, and then use setItems() to populate the Table component. " +
        "The backend uses JPA repositories and REST controllers to manage and expose the customer data."
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
    tableContainer.add(customerTable);

    container.add(header, tableContainer);
  }

  private void setupTableColumns() {
    customerTable.addColumn("ID", CustomerModel::getId).setMinWidth(80);
    customerTable.addColumn("Name", CustomerModel::getName);
    customerTable.addColumn("Email", CustomerModel::getEmail);
    customerTable.addColumn("Company", CustomerModel::getCompany);
    customerTable.addColumn("Phone", CustomerModel::getPhone);

    customerTable.addClassName("customers-table");
    customerTable.setRowHeight(45);
    customerTable.setHeight("60dvh");
  }

  private void loadData() {
    try {
      List<CustomerModel> customers = customerService.getAllCustomers();
      customerTable.setItems(customers);
    } catch (Exception e) {
      System.err.println("Error loading customer data: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
