package com.webforjrest.service;

import com.webforj.data.repository.DelegatingRepository;
import com.webforjrest.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * DelegatingRepository implementation for Customers from the REST API.
 *
 * This repository demonstrates the three required functions:
 * 1. Find function - fetches paginated data based on criteria
 * 2. Count function - returns total number of items (as int)
 * 3. Find by key function - fetches a single item by ID
 */
@Component
public class CustomerDelegatingRepository extends DelegatingRepository<Customer, Object> {

    public CustomerDelegatingRepository(RestClientService restClientService) {
        super(
            // 1. Find function: RepositoryCriteria -> Stream<Customer>
            // Extracts pagination info and fetches data from the API
            criteria -> {
                int limit = criteria.getLimit();
                int offset = criteria.getOffset();

                List<Customer> customers = restClientService.fetchCustomers(limit, offset);
                return customers != null ? customers.stream() : Stream.empty();
            },

            // 2. Count function: RepositoryCriteria -> int
            // Returns total count for pagination calculations
            criteria -> restClientService.getCustomerCount(),

            // 3. Find by key function: Object -> Optional<Customer>
            // Fetches a single customer by ID (cast key to Long)
            key -> {
                if (key instanceof Long) {
                    return restClientService.fetchCustomerById((Long) key);
                }
                return Optional.empty();
            }
        );
    }
}
