package com.example.spring6mvc.mappers;

import com.example.spring6mvc.enteties.Beer;
import com.example.spring6mvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDto(Beer beer);


}
