package com.example.spring6mvc.bootstrap;

import com.example.spring6mvc.repositories.BeerRepository;
import com.example.spring6mvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    BoostrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BoostrapData(beerRepository, customerRepository);
    }

    @Test
    void TestRun() throws Exception {
        bootstrapData.run(null);
        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}

