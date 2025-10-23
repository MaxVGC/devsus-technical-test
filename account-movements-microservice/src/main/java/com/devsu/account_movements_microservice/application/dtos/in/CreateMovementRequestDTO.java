package com.devsu.account_movements_microservice.application.dtos.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovementRequestDTO {

    @NotNull(message = "amount is required")
    private Integer amount;

    @NotNull(message = "accountId is required")
    @Min(value = 1, message = "accountId must be greater than or equal to 1")
    private Long accountId;
}
