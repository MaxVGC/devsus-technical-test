package com.devsu.account_movements_microservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    private Long id;
    private Long accountNumber;
    private Integer balance;
    private EState state;
    private Client propietary;
    private EAccountType type;
}
