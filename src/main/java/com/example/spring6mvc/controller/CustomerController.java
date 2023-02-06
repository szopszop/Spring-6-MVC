package com.example.spring6mvc.controller;


import com.example.spring6mvc.model.Customer;
import com.example.spring6mvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(value = "/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId")UUID customerId) {
        return customerService.getCustomerById(customerId);
    }
}
