package com.webforjrest.service;

import com.webforjrest.entity.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

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
     * @return List of Customer objects
     */
    public List<Customer> getAllCustomers() {
        System.out.println("Fetching all customers through REST API...");

        List<Customer> customers = restClient.get()
                .uri("/api/customers")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        System.out.println("...received " + customers.size() + " customers.");
        return customers;
    }

    /**
     * Fetches customers with pagination support.
     *
     * @param limit Number of customers to fetch
     * @param offset Starting index (0-based)
     * @return List of Customer objects
     */
    public List<Customer> fetchCustomers(int limit, int offset) {
        List<Customer> customers = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/customers/paginated")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return customers;
    }

    /**
     * Fetches a single customer by ID.
     *
     * @param id Customer ID
     * @return Optional containing the customer if found
     */
    public Optional<Customer> fetchCustomerById(Long id) {
        try {
            Customer customer = restClient.get()
                    .uri("/api/customers/" + id)
                    .retrieve()
                    .body(Customer.class);
            return Optional.ofNullable(customer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Fetches the total count of customers from the REST API.
     *
     * @return Total number of customers
     */
    public int getCustomerCount() {
        System.out.println("Fetching customer count through REST API...");

        Long count = restClient.get()
                .uri("/api/customers/count")
                .retrieve()
                .body(Long.class);

        System.out.println("...received count: " + count);
        return count != null ? count.intValue() : 0;
    }
}
