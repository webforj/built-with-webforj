package com.webforjrest.frontend.data;

import com.webforjrest.frontend.models.CustomerModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Service for consuming REST API endpoints.
 * Fetches customer data from the backend REST API.
 */
@Service
public class RestClientService {

    private final RestClient restClient;

    public RestClientService(@Value("${server.port:8080}") String serverPort) {
        this.restClient = RestClient.create("http://localhost:" + serverPort);
    }

    /**
     * Fetches all customers from the REST API.
     *
     * @return List of CustomerModel objects
     */
    public List<CustomerModel> getAllCustomers() {
        System.out.println("Fetching all customers through REST API...");

        List<CustomerModel> customers = restClient.get()
                .uri("/api/customers")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        System.out.println("...received " + customers.size() + " customers.");
        return customers;
    }

    /**
     * Fetches the total count of customers from the REST API.
     *
     * @return Total number of customers
     */
    public Long getCustomerCount() {
        System.out.println("Fetching customer count through REST API...");

        Long count = restClient.get()
                .uri("/api/customers/count")
                .retrieve()
                .body(Long.class);

        System.out.println("...received count: " + count);
        return count;
    }

    /**
     * Creates a new customer via the REST API.
     *
     * @param customer Customer to create
     * @return Created customer with ID
     */
    public CustomerModel createCustomer(CustomerModel customer) {
        System.out.println("Creating customer: " + customer.getName());

        CustomerModel created = restClient.post()
                .uri("/api/customers")
                .body(customer)
                .retrieve()
                .body(CustomerModel.class);

        System.out.println("...customer created with ID: " + created.getId());
        return created;
    }

    /**
     * Updates an existing customer via the REST API.
     *
     * @param id Customer ID
     * @param customer Updated customer data
     * @return Updated customer
     */
    public CustomerModel updateCustomer(Long id, CustomerModel customer) {
        System.out.println("Updating customer ID: " + id);

        CustomerModel updated = restClient.put()
                .uri("/api/customers/" + id)
                .body(customer)
                .retrieve()
                .body(CustomerModel.class);

        System.out.println("...customer updated");
        return updated;
    }

    /**
     * Deletes a customer via the REST API.
     *
     * @param id Customer ID to delete
     */
    public void deleteCustomer(Long id) {
        System.out.println("Deleting customer ID: " + id);

        restClient.delete()
                .uri("/api/customers/" + id)
                .retrieve()
                .toBodilessEntity();

        System.out.println("...customer deleted");
    }
}
