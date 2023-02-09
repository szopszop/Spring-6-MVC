package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

        List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
        List<Beer> findAllByBeerStyle(BeerStyle beerStyle);

        List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);

}
