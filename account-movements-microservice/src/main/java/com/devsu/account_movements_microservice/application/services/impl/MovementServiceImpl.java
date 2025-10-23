package com.devsu.account_movements_microservice.application.services.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.devsu.account_movements_microservice.application.dtos.in.CreateMovementRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.TransactionResponseDTO;
import com.devsu.account_movements_microservice.application.mappers.MovementMapper;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IMovementRepository;
import com.devsu.account_movements_microservice.application.services.IMovementService;
import com.devsu.account_movements_microservice.domain.exceptions.ApplicationException;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Movement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovementServiceImpl implements IMovementService {

    private final IAccountRepository accountRepository;
    private final IMovementRepository movementRepository;
    private final MovementMapper movementMapper = Mappers.getMapper(MovementMapper.class);

    @Override
    public Mono<TransactionResponseDTO> create(CreateMovementRequestDTO dto) {
        Mono<Account> accountMono = accountRepository.findAccountById(dto.getAccountId())
                .flatMap(optionalAccount -> optionalAccount
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new ApplicationException("Account not found"))));

        return accountMono.flatMap(account -> {
            Integer newBalance = account.getBalance() + dto.getAmount();
            if (newBalance < 0) {
                return Mono.error(new ApplicationException("Insufficient funds"));
            }

            Movement movement = movementMapper.toMovement(dto, account);
            return movementRepository.save(movement)
                    .map(savedMovement -> {
                        TransactionResponseDTO response = TransactionResponseDTO.builder()
                                .id(savedMovement.getId())
                                .amount(savedMovement.getAmount())
                                .build();

                        account.setBalance(newBalance);
                        // Note: We use subscribe here to avoid blocking the main flow, in case of error it will be logged, also we
                        // can consider other strategies like retry or dead letter queue based on requirements or reverse the operation
                        accountRepository.save(account)
                                .doOnError(error -> log.error("Failed to update balance for account {}: {}",
                                        account.getId(), error.getMessage()))
                                .subscribe();

                        return response;
                    });
        });

    }

    @Override
    public Mono<MovementResponseDTO> get(Long id) {
        Mono<Movement> movementMono = movementRepository.findByMovementId(id)
                .flatMap(optionalMovement -> optionalMovement
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new ApplicationException("Movement not found"))));
        return movementMono.map(movementMapper::toMovementResponseDTO);
    }

}
