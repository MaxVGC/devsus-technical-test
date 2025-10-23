package com.devsu.account_movements_microservice.domain.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movement {
    private Long id;
    private Integer amount;
    private EMovementType type;
    private Account account;
    private Date date;
    private Integer balance;
}
