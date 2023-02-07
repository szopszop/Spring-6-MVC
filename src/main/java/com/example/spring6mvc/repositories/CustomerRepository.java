package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {


}
