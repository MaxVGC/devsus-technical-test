package com.devsu.account_movements_microservice.application.dtos.out;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponseDTO {
    List<AccountResponseDTO> accounts;
    List<MovementResponseDTO> movements;
}
