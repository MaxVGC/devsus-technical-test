package com.devsu.account_movements_microservice.application.ports;

import java.util.List;
import java.util.Optional;

import com.devsu.account_movements_microservice.domain.models.Account;

import reactor.core.publisher.Mono;

public interface IAccountRepository {
    public Mono<Optional<Account>> findAccountById(Long id);

    public Mono<Optional<Account>> findByAccountNumber(Long accountNumber);

    public Mono<Account> save(Account account);

    public Mono<List<Account>> getByPropietaryId(Long propietaryId);
}
