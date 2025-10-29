package com.webforjrest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webforjrest.entity.Customer;
import com.webforjrest.repository.CustomerRepository;

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
}
