package com.devsu.account_movements_microservice.application.dtos.in;

import com.devsu.account_movements_microservice.domain.models.EAccountType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateAccountRequestDTO {
    private Long accountNumber;
    private Integer balance;
    private Long propietaryId;
    private EAccountType type;
}
