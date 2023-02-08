package com.example.spring6mvc.controller;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.mappers.BeerMapper;
import com.example.spring6mvc.model.BeerDTO;
import com.example.spring6mvc.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.h2.engine.Setting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.spring6mvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    MockMvc mockMvc;

    /*
     Setting up Mock MVC environment with SpringData Repository injected into the Service.
        Full SpringBoot Test
     */
    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void sizeOfRepositoryShouldBeEqualTo3() {
        List<BeerDTO> dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }


    @Transactional
    @Rollback
    @Test
    void sizeShouldBe0AfterDeleteAll() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.getAllBeers();
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

    @Transactional
    @Rollback
    @Test
    void shouldSaveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("My Beer")
                .build();

        ResponseEntity responseEntity = beerController.saveNewBeer(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }



    @Transactional
    @Rollback
    @Test
    void shouldUpdateExisingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setBeerId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateBeerById(beer.getBeerId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getBeerId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void updatingNonExistingBeerThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void deleteExistingBeerWorks() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getBeerId());
        assertThat(beerRepository.findById(beer.getBeerId()).isEmpty());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

    }

    @Test
    void deletingNonExistingBeerThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }

    @Test
    void patchingWithTooLongNameThrowsException() throws Exception {
        Beer beer = beerRepository.findAll().get(0);
        Map<String, Object> mapToPatch = new HashMap<>();
        mapToPatch.put("beerName", "TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName");
        MvcResult mvcResult = mockMvc.perform(patch(BEER_PATH_ID, beer.getBeerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapToPatch)))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        MvcResult mvcResult =mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getBeerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}