package com.devsu.account_movements_microservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.domain.models.Account;

@Mapper
public interface AccountMapper {
    Account toAccount(CreateAccountRequestDTO dto, Long propietaryId);
    @Mapping(target = "id", source = "account.id")
    @Mapping(target = "state", source = "account.state")
    Account toAccount(Account account, Long propietaryId);
    AccountResponseDTO toAccountResponseDTO(Account account);
}
