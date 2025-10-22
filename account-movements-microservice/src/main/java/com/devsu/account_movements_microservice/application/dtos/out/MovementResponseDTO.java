package com.devsu.account_movements_microservice.application.dtos.out;

import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.EMovementType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MovementResponseDTO {
    private Long id;
    private Integer amount;
    private EMovementType type;
    private Account account;
}
