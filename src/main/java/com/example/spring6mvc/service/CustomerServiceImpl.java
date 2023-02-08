package com.example.spring6mvc.service;

import com.example.spring6mvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .customerId(UUID.randomUUID())
                .customerName("Customer 1")
                .version(1L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .customerId(UUID.randomUUID())
                .customerName("Customer 2")
                .version(1L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .customerId(UUID.randomUUID())
                .customerName("Customer 3")
                .version(1L)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getCustomerId(), customer1);
        customerMap.put(customer2.getCustomerId(), customer2);
        customerMap.put(customer3.getCustomerId(), customer3);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
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
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO dto) {
        CustomerDTO existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(dto.getCustomerName());
        existingCustomer.setVersion(dto.getVersion());
        existingCustomer.setUpdateDate(LocalDateTime.now());

        customerMap.put(existingCustomer.getCustomerId(), existingCustomer);
        return Optional.of(existingCustomer);
    }

    @Override
    public Boolean deleteById(UUID customerID) {
        customerMap.remove(customerID);
        return true;
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO dto) {
        CustomerDTO existing = customerMap.get(customerId);

        if (StringUtils.hasText(dto.getCustomerName())){
            existing.setCustomerName(dto.getCustomerName());
        }
        dto.setUpdateDate(LocalDateTime.now());
    }


}
