package com.webforjrest.frontend.views;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.table.Table;
import com.webforj.component.table.Column.PinDirection;
import com.webforj.component.table.renderer.IconRenderer;
import com.webforj.router.annotation.Route;
import com.webforjrest.frontend.components.CustomerDialog;
import com.webforjrest.frontend.data.RestClientService;
import com.webforjrest.frontend.models.CustomerModel;

import java.util.List;

/**
 * Main view for managing customers.
 * Follows the pattern from webforj-crud example.
 */
@Route("/")
public class CustomerView extends Composite<FlexLayout> {

    private final RestClientService customerService;

    private FlexLayout container = getBoundComponent();
    private FlexLayout header;
    private FlexLayout toolbar;
    private FlexLayout tableContainer;

    private H1 pageTitle;
    private Button addButton;
    private Table<CustomerModel> customerTable;
    private CustomerDialog customerDialog;

    public CustomerView(RestClientService customerService) {
        this.customerService = customerService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        initializeDialog();
        loadData();
    }

    private void initializeComponents() {
        pageTitle = new H1("Customer Management");

        addButton = new Button("Add New")
                .setTheme(ButtonTheme.PRIMARY)
                .setPrefixComponent(FeatherIcon.PLUS.create());

        customerTable = new Table<>();
        setupTableColumns();
    }

    private void setupLayout() {
        container.setDirection(FlexDirection.COLUMN);

        header = new FlexLayout();
        header.setDirection(FlexDirection.COLUMN);
        toolbar = new FlexLayout();
        toolbar.add(addButton);

        tableContainer = new FlexLayout();
        tableContainer.setDirection(FlexDirection.COLUMN);

        header.add(pageTitle);
        tableContainer.add(customerTable);
        container.add(header, toolbar, tableContainer);
    }

    private void setupTableColumns() {
        customerTable.addColumn("ID", CustomerModel::getId).setMinWidth(80);
        customerTable.addColumn("Name", CustomerModel::getName);
        customerTable.addColumn("Email", CustomerModel::getEmail);
        customerTable.addColumn("Company", CustomerModel::getCompany);
        customerTable.addColumn("Phone", CustomerModel::getPhone);

        customerTable.addColumn("", new IconRenderer<CustomerModel>("edit", "feather", e -> {
            CustomerModel customer = e.getItem();
            customerDialog.showDialog(customer);
        }))
                .setMinWidth(50.0f)
                .setPinDirection(PinDirection.RIGHT);

        customerTable.addClassName("customers-table");
        customerTable.setRowHeight(45);
		customerTable.setHeight("50dvh");
    }

    private void setupEventHandlers() {
        addButton.addClickListener(e -> customerDialog.showDialog());
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

    private void initializeDialog() {
        customerDialog = new CustomerDialog(customerService, this::loadData);
        container.add(customerDialog);
    }
}
