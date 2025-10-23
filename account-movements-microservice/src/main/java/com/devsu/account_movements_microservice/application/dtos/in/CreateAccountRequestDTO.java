package com.devsu.account_movements_microservice.application.dtos.in;

import com.devsu.account_movements_microservice.domain.models.EAccountType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateAccountRequestDTO {

    @NotNull(message = "accountNumber is required")
    @Min(value = 1, message = "accountNumber must be greater than or equal to 1")
    private Long accountNumber;

    @NotNull(message = "balance is required")
    @PositiveOrZero(message = "balance must be zero or positive")
    private Integer balance;

    @NotNull(message = "propietaryId is required")
    @Min(value = 1, message = "propietaryId must be greater than or equal to 1")
    private Long propietaryId;

    @NotNull(message = "type is required")
    @Pattern(regexp = "^(?i)(SAVINGS|CHECKING)$", message = "Account type must be 'SAVINGS' or 'CHECKING'")
    private EAccountType type;
}
