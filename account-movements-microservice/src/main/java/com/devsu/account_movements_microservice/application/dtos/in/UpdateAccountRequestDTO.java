package com.devsu.account_movements_microservice.application.dtos.in;

import com.devsu.account_movements_microservice.domain.models.EAccountType;
import com.devsu.account_movements_microservice.domain.models.EState;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequestDTO {

    @NotNull(message = "accountNumber is required")
    @Min(value = 1, message = "accountNumber must be greater than or equal to 1")
    private Long accountNumber;

    @Pattern(regexp = "^(?i)(ACTIVE|INACTIVE)$", message = "Account state must be 'ACTIVE' or 'INACTIVE'")
    @NotNull(message = "state is required")
    private EState state;

    @Pattern(regexp = "^(?i)(SAVINGS|CHECKING)$", message = "Account type must be 'SAVINGS' or 'CHECKING'")
    @NotNull(message = "type is required")
    private EAccountType type;
}
