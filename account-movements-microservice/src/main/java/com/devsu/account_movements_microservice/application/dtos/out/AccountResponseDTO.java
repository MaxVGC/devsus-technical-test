package com.devsu.account_movements_microservice.application.dtos.out;

import com.devsu.account_movements_microservice.domain.models.EAccountType;
import com.devsu.account_movements_microservice.domain.models.EState;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountResponseDTO {
    private Long id;
    private Long accountNumber;
    private Integer balance;
    private EState state;
    private Long propietaryId;
    private EAccountType type;
}
