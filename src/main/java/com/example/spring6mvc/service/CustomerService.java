package com.example.spring6mvc.service;

import com.example.spring6mvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();
    Optional<CustomerDTO> getCustomerById(UUID customerId);

    CustomerDTO saveNewCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO dto);

    Boolean deleteById(UUID customerID);

    void patchCustomerById(UUID customerId, CustomerDTO dto);
}
