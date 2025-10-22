package com.devsu.account_movements_microservice.application.dtos.in;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovementRequestDTO {
    private Integer amount;
    private Long accountId;
}
