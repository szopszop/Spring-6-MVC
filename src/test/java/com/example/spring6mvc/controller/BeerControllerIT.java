package com.example.spring6mvc.controller;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.mappers.BeerMapper;
import com.example.spring6mvc.model.BeerDTO;
import com.example.spring6mvc.model.BeerStyle;
import com.example.spring6mvc.repositories.BeerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.spring6mvc.controller.BeerController.BEER_PATH;
import static com.example.spring6mvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testNoAuth() throws Exception {
            mockMvc.perform(get(BeerController.BEER_PATH)
                            .queryParam("beerStyle", BeerStyle.IPA.name())
                            .queryParam("pageSize", "800"))
                    .andExpect(status().isUnauthorized());

    }

    @Test
    void sizeOfRepositoryShouldBeEqualTo3() {
        Page<BeerDTO> dtos = beerController.getListOfBeers(null, null, false, null, null);
        int defaultPageSize = 25;
        assertThat(dtos.getSize()).isEqualTo(defaultPageSize);
    }


    @Transactional
    @Rollback
    @Test
    void sizeShouldBe0AfterDeleteAll() {
        beerRepository.deleteAll();
        Page<BeerDTO> dtos = beerController.getListOfBeers(null, null, false, 1, 25);
        assertThat(dtos.getContent().size()).isEqualTo(0);
    }

    @Test
    void getBeerByIdShouldReturnBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());

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
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
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
        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(beerRepository.findById(beer.getId()).isEmpty());
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
        MvcResult mvcResult = mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapToPatch)))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testListBeerByName() throws Exception {
        int numberOfIpaBeersNamesInRepo = 336;
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(numberOfIpaBeersNamesInRepo)));
    }

    @Test
    void testListBeersByStyle() throws Exception {
        int numberOfIpaBeersEnumsInRepo = 548;
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(numberOfIpaBeersEnumsInRepo)));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        int numberOfIpaBeersEnumsInRepo = 310;
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(numberOfIpaBeersEnumsInRepo)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        int numberOfIpaBeersEnumsInRepo = 310;
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(numberOfIpaBeersEnumsInRepo)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }
    @Test
    void tesListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(httpBasic(BeerControllerTest.USERNAME, BeerControllerTest.PASSWORD))
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

}