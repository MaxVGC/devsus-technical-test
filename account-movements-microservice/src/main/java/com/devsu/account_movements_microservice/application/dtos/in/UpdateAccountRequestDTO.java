package com.devsu.account_movements_microservice.application.dtos.in;

import com.devsu.account_movements_microservice.domain.models.EAccountType;
import com.devsu.account_movements_microservice.domain.models.EState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequestDTO {
    private Long accountNumber;
    private EState state;
    private EAccountType type;
}
