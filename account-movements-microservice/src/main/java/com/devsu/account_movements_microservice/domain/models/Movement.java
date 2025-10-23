package com.devsu.account_movements_microservice.domain.models;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movement {
    private Long id;
    private Integer amount;
    private EMovementType type;
    private Account account;
    private Date date;
}
