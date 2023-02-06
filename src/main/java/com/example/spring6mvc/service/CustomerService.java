package com.example.spring6mvc.service;

import com.example.spring6mvc.model.Beer;
import com.example.spring6mvc.model.Customer;

import java.util.List;
import java.util.UUID;


public interface CustomerService {

    List<Customer> listCustomers();
    Customer getCustomerById(UUID customerId);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteById(UUID customerID);
}
