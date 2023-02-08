package com.example.spring6mvc.service;

import com.example.spring6mvc.model.BeerDTO;
import com.example.spring6mvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();


        BeerDTO beer1 = BeerDTO.builder()
                .beerId(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .beerId(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .beerId(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getBeerId(), beer1);
        beerMap.put(beer2.getBeerId(), beer2);
        beerMap.put(beer3.getBeerId(), beer3);

    }

    @Override
    public List<BeerDTO> getAllBeers(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        BeerDTO beerToSave =  BeerDTO.builder()
                .beerId(UUID.randomUUID())
                .beerName(beer.getBeerName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .beerStyle(beer.getBeerStyle())
                .price(beer.getPrice())
                .build();

        beerMap.put(beerToSave.getBeerId(), beerToSave);
        return beerToSave;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO dto) {
        BeerDTO existingBeer = beerMap.get(beerId);
        existingBeer.setBeerName(dto.getBeerName());
        existingBeer.setPrice(dto.getPrice());
        existingBeer.setUpc(dto.getUpc());
        existingBeer.setUpdateDate(LocalDateTime.now());

        beerMap.put(existingBeer.getBeerId(), existingBeer);
        return Optional.of(existingBeer);
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO dto) {
        BeerDTO existing = beerMap.get(beerId);

        if (StringUtils.hasText(dto.getBeerName())){
            existing.setBeerName(dto.getBeerName());
        }

        if (dto.getBeerStyle() != null) {
            existing.setBeerStyle(dto.getBeerStyle());
        }

        if (dto.getPrice() != null) {
            existing.setPrice(dto.getPrice());
        }

        if (dto.getQuantityOnHand() != null){
            existing.setQuantityOnHand(dto.getQuantityOnHand());
        }

        if (StringUtils.hasText(dto.getUpc())) {
            existing.setUpc(dto.getUpc());
        }
        return Optional.of(existing);
    }
}