package com.devsu.account_movements_microservice.application.dtos.out;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private String message;
    private String code;
}

