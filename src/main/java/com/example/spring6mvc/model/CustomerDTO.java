package com.example.spring6mvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {

    private UUID customerId;

    private String customerName;
    private Integer version;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
