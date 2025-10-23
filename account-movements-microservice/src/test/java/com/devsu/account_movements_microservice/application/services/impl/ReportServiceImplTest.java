package com.devsu.account_movements_microservice.application.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.devsu.account_movements_microservice.application.dtos.in.QueryReportDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.ReportResponseDTO;
import com.devsu.account_movements_microservice.application.mappers.AccountMapper;
import com.devsu.account_movements_microservice.application.mappers.MovementMapper;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IMovementRepository;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Movement;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    IAccountRepository accountRepository;

    @Mock
    IMovementRepository movementRepository;

    @Mock
    MovementMapper movementMapperMock;

    @Mock
    AccountMapper accountMapperMock;

    ReportServiceImpl service;

    @BeforeEach
    void setup() throws Exception {
        service = new ReportServiceImpl(accountRepository, movementRepository);
        Field mapperField = ReportServiceImpl.class.getDeclaredField("movementMapper");
        mapperField.setAccessible(true);
        mapperField.set(service, movementMapperMock);
        Field accountMapperField = ReportServiceImpl.class.getDeclaredField("accountMapper");
        accountMapperField.setAccessible(true);
        accountMapperField.set(service, accountMapperMock);
    }

    @Test
    void generateReport_shouldReturnReportWithSanitizedFields_whenRepositoriesReturnData() {

        Account acct = new Account();
        Movement mov = new Movement();

        AccountResponseDTO acctDTO = new AccountResponseDTO();

        when(accountRepository.getByPropietaryId(any())).thenReturn(Mono.just(List.of(acct)));
        when(movementRepository.findByDatesAndPropietaryId(any())).thenReturn(Mono.just(List.of(mov)));
        when(accountMapperMock.toAccountResponseDTO(acct)).thenReturn(acctDTO);
        when(movementMapperMock.toMovementResponseDTO(mov)).thenReturn(new MovementResponseDTO());

        Mono<ReportResponseDTO> result = service.generateReport(new QueryReportDTO());

        StepVerifier.create(result)
                .assertNext(report -> {
                    assertNotNull(report);
                    assertNotNull(report.getAccounts());
                    assertEquals(1, report.getAccounts().size());
                    assertNull(report.getAccounts().get(0).getPropietaryId(), "propietaryId must be nullified");
                    assertNotNull(report.getMovements());
                    assertEquals(1, report.getMovements().size());
                    assertNull(report.getMovements().get(0).getAccount(), "movement.account must be nullified");
                })
                .verifyComplete();
    }

    @Test
    void generateReport_shouldReturnEmptyLists_whenRepositoriesReturnEmpty() {
        when(accountRepository.getByPropietaryId(any())).thenReturn(Mono.just(List.of()));
        when(movementRepository.findByDatesAndPropietaryId(any())).thenReturn(Mono.just(List.of()));

        Mono<ReportResponseDTO> result = service.generateReport(new QueryReportDTO());

        StepVerifier.create(result)
                .assertNext(report -> {
                    assertNotNull(report);
                    assertNotNull(report.getAccounts());
                    assertTrue(report.getAccounts().isEmpty());
                    assertNotNull(report.getMovements());
                    assertTrue(report.getMovements().isEmpty());
                })
                .verifyComplete();
    }
}