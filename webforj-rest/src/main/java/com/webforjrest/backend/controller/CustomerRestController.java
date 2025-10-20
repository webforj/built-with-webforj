package com.webforjrest.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.webforjrest.backend.entity.Customer;
import com.webforjrest.backend.repository.CustomerRepository;

import java.util.List;

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
     * REST API for getting the total count of customers
     *
     * GET http://localhost:8080/api/customers/count
     */
    @GetMapping("/count")
    public long getCustomerCount() {
        return customerRepository.count();
    }

    /**
     * REST API for creating a new customer
     *
     * POST http://localhost:8080/api/customers
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    /**
     * REST API for updating an existing customer
     *
     * PUT http://localhost:8080/api/customers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        Customer updatedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * REST API for deleting a customer
     *
     * DELETE http://localhost:8080/api/customers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
