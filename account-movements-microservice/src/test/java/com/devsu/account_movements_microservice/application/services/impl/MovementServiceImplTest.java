package com.devsu.account_movements_microservice.application.services.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.devsu.account_movements_microservice.application.dtos.in.CreateMovementRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.application.mappers.MovementMapper;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IMovementRepository;
import com.devsu.account_movements_microservice.domain.exceptions.ApplicationException;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.EMovementType;
import com.devsu.account_movements_microservice.domain.models.Movement;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MovementServiceImplTest {

    @Mock
    IAccountRepository accountRepository;

    @Mock
    IMovementRepository movementRepository;

    @Mock
    MovementMapper movementMapperMock;

    MovementServiceImpl service;

    @BeforeEach
    void setup() throws Exception {
        service = new MovementServiceImpl(accountRepository, movementRepository);

        Field mapperField = MovementServiceImpl.class.getDeclaredField("movementMapper");
        mapperField.setAccessible(true);
        mapperField.set(service, movementMapperMock);
    }

    @Test
    void create_success_shouldReturnTransactionResponseAndUpdateAccountAsync() throws Exception {
        CreateMovementRequestDTO dto = org.mockito.Mockito.mock(CreateMovementRequestDTO.class);
        when(dto.getAccountId()).thenReturn(1L);
        when(dto.getAmount()).thenReturn(50);

        Account account = org.mockito.Mockito.mock(Account.class);
        when(account.getBalance()).thenReturn(100);
        when(account.getId()).thenReturn(1L);

        when(accountRepository.findAccountById(1L)).thenReturn(Mono.just(Optional.of(account)));

        Movement movementFromMapper = org.mockito.Mockito.mock(Movement.class);

        Date dateVal = Date.from(Instant.now());
        when(movementFromMapper.getDate()).thenReturn(dateVal);

        EMovementType typeMock = org.mockito.Mockito.mock(EMovementType.class);
        when(typeMock.toString()).thenReturn("CREDIT");
        when(movementFromMapper.getType()).thenReturn(typeMock);

        when(movementFromMapper.getAmount()).thenReturn(50);

        when(movementMapperMock.toMovement(eq(dto), eq(account))).thenReturn(movementFromMapper);

        Movement savedMovement = org.mockito.Mockito.mock(Movement.class);
        when(savedMovement.getId()).thenReturn(123L);
        when(savedMovement.getAmount()).thenReturn(50);

        when(movementRepository.save(movementFromMapper)).thenReturn(Mono.just(savedMovement));
        when(accountRepository.save(account)).thenReturn(Mono.empty()); 
        StepVerifier.create(service.create(dto))
                .expectNextMatches(tr -> tr.getId().equals(123L)
                        && tr.getAmount().equals(50)
                        && "CREDIT".equals(tr.getType())
                        && tr.getDate().equals(dateVal))
                .verifyComplete();
    }

    @Test
    void create_insufficientFunds_shouldEmitApplicationException() {
        CreateMovementRequestDTO dto = org.mockito.Mockito.mock(CreateMovementRequestDTO.class);
        when(dto.getAccountId()).thenReturn(1L);
        when(dto.getAmount()).thenReturn(-200); // withdrawal

        Account account = org.mockito.Mockito.mock(Account.class);
        when(account.getBalance()).thenReturn(100);

        when(accountRepository.findAccountById(1L)).thenReturn(Mono.just(Optional.of(account)));

        StepVerifier.create(service.create(dto))
                .expectErrorSatisfies(err -> {
                    assert err instanceof ApplicationException;
                    assert err.getMessage().contains("Insufficient funds");
                })
                .verify();
    }

    @Test
    void create_accountNotFound_shouldEmitApplicationException() {
        CreateMovementRequestDTO dto = org.mockito.Mockito.mock(CreateMovementRequestDTO.class);
        when(dto.getAccountId()).thenReturn(42L);
        when(accountRepository.findAccountById(42L)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.create(dto))
                .expectErrorSatisfies(err -> {
                    assert err instanceof ApplicationException;
                    assert err.getMessage().contains("Account not found");
                })
                .verify();
    }

    @Test
    void get_existingMovement_shouldReturnMovementResponseDTO() {
        Movement movement = org.mockito.Mockito.mock(Movement.class);
        when(movementRepository.findByMovementId(10L)).thenReturn(Mono.just(Optional.of(movement)));

        MovementResponseDTO responseDto = org.mockito.Mockito.mock(MovementResponseDTO.class);
        when(movementMapperMock.toMovementResponseDTO(movement)).thenReturn(responseDto);

        StepVerifier.create(service.get(10L))
                .expectNext(responseDto)
                .verifyComplete();
    }

    @Test
    void get_movementNotFound_shouldEmitApplicationException() {
        when(movementRepository.findByMovementId(99L)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.get(99L))
                .expectErrorSatisfies(err -> {
                    assert err instanceof ApplicationException;
                    assert err.getMessage().contains("Movement not found");
                })
                .verify();
    }
}