package com.webforjrest.service;

import com.webforjrest.entity.Customer;
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
}
