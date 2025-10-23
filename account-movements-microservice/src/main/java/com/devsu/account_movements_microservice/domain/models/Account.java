package com.devsu.account_movements_microservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private Long accountNumber;
    private Integer balance;
    private EState state;
    private Long propietaryId;
    private EAccountType type;
}
