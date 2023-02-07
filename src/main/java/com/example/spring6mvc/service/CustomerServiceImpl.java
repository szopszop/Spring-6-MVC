package com.example.spring6mvc.service;

import com.example.spring6mvc.model.CustomerDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .customerId(UUID.randomUUID())
                .customerName("Radek")
                .version(432L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .customerId(UUID.randomUUID())
                .customerName("Waclaw")
                .version(123L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

                CustomerDTO customer3 = CustomerDTO.builder()
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
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return Optional.of(customerMap.get(customerId));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        CustomerDTO newCustomer = CustomerDTO.builder()
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
    public void updateCustomerById(UUID customerId, CustomerDTO customer) {
        CustomerDTO existingCustomer = customerMap.get(customerId);
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
