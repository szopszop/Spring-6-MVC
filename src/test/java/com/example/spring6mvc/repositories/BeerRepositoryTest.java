package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My New Beer")
                .build());

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerId()).isNotNull();
    }

}