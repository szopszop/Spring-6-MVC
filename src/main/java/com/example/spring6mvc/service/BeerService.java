package com.example.spring6mvc.service;

import com.example.spring6mvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    void updateById(UUID beerId, BeerDTO beer);

    void deleteById(UUID beerId);

    void patchById(UUID beerId, BeerDTO beer);
}