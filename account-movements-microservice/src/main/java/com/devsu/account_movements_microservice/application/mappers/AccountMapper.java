package com.devsu.account_movements_microservice.application.mappers;

import org.mapstruct.Mapper;

import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Client;

@Mapper
public interface AccountMapper {
    Account toAccount(CreateAccountRequestDTO dto, Client client);
    AccountResponseDTO toAccountResponseDTO(Account account);
}
