package com.example.spring6mvc.bootstrap;

import com.example.spring6mvc.repositories.BeerRepository;
import com.example.spring6mvc.repositories.CustomerRepository;
import com.example.spring6mvc.service.BeerCSVService;
import com.example.spring6mvc.service.BeerCSVServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(BeerCSVServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCSVService csvService;

    BoostrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BoostrapData(beerRepository, customerRepository, csvService);
    }

    @Test
    void TestRun() throws Exception {
        bootstrapData.run(null);

        int csvFileSize = 2410;
        int staticData = 3;
        assertThat(beerRepository.count()).isEqualTo(csvFileSize + staticData);
        assertThat(customerRepository.count()).isEqualTo(staticData);
    }
}
