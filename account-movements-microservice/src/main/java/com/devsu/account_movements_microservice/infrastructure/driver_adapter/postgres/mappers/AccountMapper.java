package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.AccountEntity;

@Mapper
public interface AccountMapper {
    AccountEntity toAccountEntity(Account account);
    @Mapping(target ="id", source = "accountEntity.id")
    @Mapping(target = "state", source = "accountEntity.state")
    Account toAccount(AccountEntity accountEntity);
}
