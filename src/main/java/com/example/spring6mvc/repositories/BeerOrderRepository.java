package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {


}
