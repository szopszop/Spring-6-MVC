package com.example.spring6mvc.enteties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    UUID customerId;
    String customerName;

    @Version
    Long version;
    LocalDateTime createDate;
    LocalDateTime updateDate;
}
