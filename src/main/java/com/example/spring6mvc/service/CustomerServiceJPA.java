package com.example.spring6mvc.service;

import com.example.spring6mvc.mappers.CustomerMapper;
import com.example.spring6mvc.model.CustomerDTO;
import com.example.spring6mvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(
                customerRepository.findById(customerId).orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO dto) {
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(dto)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO dto) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(customerToUpdate -> {
            customerToUpdate.setCustomerName(dto.getCustomerName());
            customerToUpdate.setUpdateDate(LocalDateTime.now());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(customerToUpdate))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }


    @Override
    public Boolean deleteById(UUID customerID) {
        if (customerRepository.existsById(customerID)) {
            customerRepository.deleteById(customerID);
            return true;
        }
        return false;
    }

    @Override
    public void patchCustomerById(UUID customerId, CustomerDTO dto) {

    }
}
