package com.example.spring6mvc.controller;

import com.example.spring6mvc.config.SpringSecurityConfig;
import com.example.spring6mvc.model.BeerDTO;
import com.example.spring6mvc.model.BeerStyle;
import com.example.spring6mvc.service.BeerService;
import com.example.spring6mvc.service.BeerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.example.spring6mvc.controller.BeerController.BEER_PATH;
import static com.example.spring6mvc.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@Import(SpringSecurityConfig.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Value("${spring.security.user.name}")
    String username = "user1";
    @Value("${spring.security.user.password}")
    String password = "password";


    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void shouldReturnBeer() throws Exception {
        BeerDTO testBeer = beerServiceImpl.getListOfBeers(
                null, null, false, 1, 25).getContent().get(0);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
        mockMvc.perform(get(BEER_PATH_ID, testBeer.getId())
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // $ is root direction
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }


    @Test
    void shouldReturnAllBeers() throws Exception {
        given(beerService.getListOfBeers(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.getListOfBeers(null, null, false, null, null));
        mockMvc.perform(get(BEER_PATH)
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    void shouldCreateNewBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, null, null).getContent().get(0);
        beer.setVersion(null);
        beer.setId(null);
        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.getListOfBeers(null, null, false, null, null).getContent().get(1));

        mockMvc.perform(post(BEER_PATH)
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void shouldSendBadRequestAfterSavingNewBeerWithAllNullRequiredArgs() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();
        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldSendBadRequestAfterSavingNewBeerWithNullUpc() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New name")
                .upc(null)
                .build();
        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldSendBadRequestAfterUpdatingBeerWithBlankName() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        beerDTO.setBeerName(" ");
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerDTO));

        MvcResult mvcResult = mockMvc.perform(put(BEER_PATH_ID, beerDTO.getId())
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldSendBadRequestAfterUpdatingBeerWithNullUpc() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        beerDTO.setUpc(" ");
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beerDTO));

        MvcResult mvcResult = mockMvc.perform(put(BEER_PATH_ID, beerDTO.getId())
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BEER_PATH_ID, beer.getId())
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void checkIf_UUID_IsProperlyPassed() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        given(beerService.deleteBeerById(any())).willReturn(true);
        mockMvc.perform(delete(BEER_PATH_ID, beer.getId())
                        .with (httpBasic(username, password))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beerDTO.getId())
                        .with (httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }


    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }


}