package com.devsu.account_movements_microservice.application.dtos.out;

import java.util.Date;

import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.EMovementType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
    @JsonInclude(Include.NON_NULL)
    private Account account;
    private Date date;
    private Integer balance;
}
