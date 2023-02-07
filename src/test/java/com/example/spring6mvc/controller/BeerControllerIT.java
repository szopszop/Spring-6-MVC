package com.example.spring6mvc.controller;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.model.BeerDTO;
import com.example.spring6mvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void sizeOfRepositoryShouldBeEqualTo3() {
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }


    @Transactional
    @Rollback
    @Test
    void sizeShouldBe0AfterDeleteAll() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void getBeerByIdShouldReturnBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getBeerId());

        assertThat(dto).isNotNull();
    }

    @Test
    void getBeerByIdShouldThrowExceptionWhenNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }
}