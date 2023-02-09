package com.example.spring6mvc.service;

import com.example.spring6mvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getListOfBeers(String beerName);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO dto);

    Boolean deleteBeerById(UUID beerId);

    Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO dto);
}