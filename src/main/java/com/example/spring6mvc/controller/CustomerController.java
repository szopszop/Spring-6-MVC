package com.example.spring6mvc.controller;


import com.example.spring6mvc.model.Customer;
import com.example.spring6mvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity addCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getCustomerId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable("customerId") UUID customerID) {
        customerService.deleteById(customerID);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
