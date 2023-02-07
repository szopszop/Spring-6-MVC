package com.example.spring6mvc.mappers;

import com.example.spring6mvc.enteties.Customer;
import com.example.spring6mvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
