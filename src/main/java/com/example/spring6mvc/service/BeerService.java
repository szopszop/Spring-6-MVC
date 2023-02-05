package com.example.spring6mvc.service;

import com.example.spring6mvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID id);
    List<Beer> getBeers();

}
