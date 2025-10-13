package com.webforjrest.frontend.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.router.annotation.Route;
import com.webforjrest.frontend.components.CustomerTable;
import com.webforjrest.frontend.data.RestClientService;
import com.webforjrest.frontend.models.CustomerModel;

import java.util.List;

/**
 * Customer view that displays customer data in a table.
 * Fetches data from the REST API on initialization.
 */
@Route("/")
public class CustomerView extends Composite<Div> {

	private final RestClientService restClientService;
	private final CustomerTable customerTable;

	public CustomerView(RestClientService restClientService) {
		this.restClientService = restClientService;
		this.customerTable = new CustomerTable();

		getBoundComponent().addClassName("customer-view");

		// Navigation

		H1 title = new H1("Customer Management");
		title.addClassName("view-title");

		getBoundComponent().add(title, customerTable);

		// Load data
		loadCustomerData();
	}

	private void loadCustomerData() {
		try {
			List<CustomerModel> customers = restClientService.getAllCustomers();
			customerTable.loadCustomers(customers);
		} catch (Exception e) {
			System.err.println("Error loading customer data: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
