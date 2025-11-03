package com.webforjrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webforjrest.entity.Customer;
import com.webforjrest.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * REST API for getting all customers
     *
     * GET http://localhost:8080/api/customers
     */
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * REST API for getting paginated customers
     *
     * GET http://localhost:8080/api/customers/paginated?limit=15&offset=0
     */
    @GetMapping("/paginated")
    public List<Customer> getCustomersPaginated(
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return customerRepository.findAll(pageable).getContent();
    }

    /**
     * REST API for getting a single customer by ID
     *
     * GET http://localhost:8080/api/customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * REST API for getting the total count of customers
     *
     * GET http://localhost:8080/api/customers/count
     */
    @GetMapping("/count")
    public long getCustomerCount() {
        return customerRepository.count();
    }
}
