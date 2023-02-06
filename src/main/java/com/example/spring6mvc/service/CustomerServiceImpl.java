package com.example.spring6mvc.service;

import com.example.spring6mvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        Customer customer1 = Customer.builder()
                .customerId(UUID.randomUUID())
                .customerName("Radek")
                .version(432L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerId(UUID.randomUUID())
                .customerName("Waclaw")
                .version(123L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

                Customer customer3 = Customer.builder()
                .customerId(UUID.randomUUID())
                .customerName("Rohan")
                .version(111123L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getCustomerId(),customer1);
        customerMap.put(customer2.getCustomerId(),customer2);
        customerMap.put(customer3.getCustomerId(),customer3);
    }

    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        return customerMap.get(customerId);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer newCustomer = Customer.builder()
                .customerId(UUID.randomUUID())
                .customerName("New Customer")
                .version(12333L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(newCustomer.getCustomerId(), customer);
        return newCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion());
        existingCustomer.setUpdateDate(LocalDateTime.now());

        customerMap.put(existingCustomer.getCustomerId(), existingCustomer);
    }

    @Override
    public void deleteById(UUID customerID) {
        customerMap.remove(customerID);
    }


}
