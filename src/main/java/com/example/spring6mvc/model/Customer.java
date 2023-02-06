package com.example.spring6mvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Customer {

    UUID customerId;
    String customerName;
    Long version;
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;


}
