package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void shouldSaveBeerSuccessfully() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My New Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("3241")
                .price(new BigDecimal("9.99"))
                .build());

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getBeerId()).isNotNull();
    }

    @Test
    void tooLongBeerNameShouldThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName")
                    .beerStyle(BeerStyle.IPA)
                    .upc("3241")
                    .price(new BigDecimal("9.99"))
                    .build());
            beerRepository.flush();
        });

    }

}