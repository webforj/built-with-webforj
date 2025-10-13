package com.webforjrest.frontend.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.table.Table;
import com.webforjrest.frontend.models.CustomerModel;

import java.util.List;

/**
 * Reusable table component for displaying customer data.
 */
public class CustomerTable extends Composite<Div> {

    Table<CustomerModel> table = new Table<>();

    public CustomerTable() {
        getBoundComponent().add(table);
        configureTable();
    }

    private void configureTable() {
        // Configure table columns
        table.addColumn("ID", CustomerModel::getId);
        table.addColumn("Name", CustomerModel::getName);
        table.addColumn("Email", CustomerModel::getEmail);
        table.addColumn("Company", CustomerModel::getCompany);
        table.addColumn("Phone", CustomerModel::getPhone);

        // Table styling
        table.addClassName("customer-table");
        table.setHeight("50vh");
    }

    /**
     * Load customer data into the table.
     *
     * @param customers List of customers to display
     */
    public void loadCustomers(List<CustomerModel> customers) {
        table.setItems(customers);
    }
}
