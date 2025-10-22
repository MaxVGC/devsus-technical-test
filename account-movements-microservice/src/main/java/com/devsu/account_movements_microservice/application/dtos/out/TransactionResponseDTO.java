package com.devsu.account_movements_microservice.application.dtos.out;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionResponseDTO {
    private Long id;
    private Integer amount;
    private String type;
    private Date date;
}
