package com.example.spring6mvc.controller;

import com.example.spring6mvc.model.BeerDTO;
import com.example.spring6mvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH ="/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public List<BeerDTO> getAllBeers(){
        return beerService.getAllBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity saveNewBeer(@Validated @RequestBody BeerDTO dto) {
        BeerDTO savedBeer = beerService.saveNewBeer(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getBeerId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO dto) {
        if (beerService.updateBeerById(beerId, dto).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId) {
        if (!beerService.deleteBeerById(beerId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO dto) {
        beerService.patchBeerById(beerId, dto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}