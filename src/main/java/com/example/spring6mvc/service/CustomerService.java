package com.example.spring6mvc.service;

import com.example.spring6mvc.model.Customer;

import java.util.List;
import java.util.UUID;


public interface CustomerService {

    List<Customer> listCustomers();
    Customer getCustomerById(UUID customerId);
}
