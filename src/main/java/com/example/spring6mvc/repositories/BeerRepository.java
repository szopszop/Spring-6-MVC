package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
