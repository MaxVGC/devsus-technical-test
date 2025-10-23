package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres;

import java.util.List;
import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.devsu.account_movements_microservice.application.dtos.in.QueryReportDTO;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IClientRepository;
import com.devsu.account_movements_microservice.application.ports.IMovementRepository;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Movement;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.AccountEntity;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.MovementEntity;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.mappers.AccountMapper;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.mappers.MovementMapper;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.repositories.AccountRepository;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.repositories.MovementRepository;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.specifications.ReportSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostgresAdapter implements IMovementRepository, IAccountRepository {
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    private final MovementMapper movementMapper = Mappers.getMapper(MovementMapper.class);

    @Override
    public Mono<Account> save(Account account) {
        return Mono.fromCallable(() -> {
            AccountEntity accountEntity = accountMapper.toAccountEntity(account);
            AccountEntity savedEntity = accountRepository.save(accountEntity);
            return accountMapper.toAccount(savedEntity);
        });
    }

    @Override
    public Mono<List<Account>> getByPropietaryId(Long propietaryId) {
        return Mono.fromCallable(() -> {
            List<AccountEntity> accountEntities = accountRepository.findByPropietaryId(propietaryId);
            return accountEntities.stream()
                    .map(accountMapper::toAccount)
                    .toList();
        });
    }

    @Override
    public Mono<Movement> save(Movement movement) {
        return Mono.fromCallable(() -> {
            MovementEntity movementEntity = movementMapper.toMovementEntity(movement);
            MovementEntity savedEntity = movementRepository.save(movementEntity);
            return movementMapper.toMovement(savedEntity);
        });
    }

    @Override
    public Mono<Optional<Account>> findAccountById(Long id) {
        return Mono.fromCallable(() -> {
            Optional<AccountEntity> accountEntityOpt = accountRepository.findById(id);
            return accountEntityOpt.map(accountMapper::toAccount);
        });
    }

    @Override
    public Mono<Optional<Movement>> findByMovementId(Long id) {
        return Mono.fromCallable(() -> {
            Optional<MovementEntity> movementEntityOpt = movementRepository.findById(id);
            return movementEntityOpt.map(movementMapper::toMovement);
        });
    }

    @Override
    public Mono<Optional<Account>> findByAccountNumber(Long accountNumber) {
        return Mono.fromCallable(() -> {
            Optional<AccountEntity> accountEntityOpt = accountRepository.findByAccountNumber(accountNumber);
            return accountEntityOpt.map(accountMapper::toAccount);
        });
    }

    @Override
    public Mono<List<Movement>> findByDatesAndPropietaryId(QueryReportDTO query) {
        return Mono.fromCallable(() -> {
            List<MovementEntity> movementEntities = movementRepository.findAll(ReportSpecification.withFilters(query));    
            return movementEntities.stream()
                    .map(movementMapper::toMovement)
                    .toList();
        });
    }

}
