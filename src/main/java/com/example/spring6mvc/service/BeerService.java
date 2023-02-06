package com.example.spring6mvc.service;

import com.example.spring6mvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateById(UUID beerId, Beer beer);

    void deleteById(UUID beerId);

    void patchById(UUID beerId, Beer beer);
}