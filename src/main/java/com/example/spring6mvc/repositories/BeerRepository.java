package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

        Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);
        Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);
        Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
        Beer findAllByUpc(String upc);
}