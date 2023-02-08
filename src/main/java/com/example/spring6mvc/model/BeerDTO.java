package com.example.spring6mvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@ToString
public class BeerDTO {
    private UUID beerId;
    private Integer version;

    @NotBlank
    @NotNull
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    private String upc;
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
