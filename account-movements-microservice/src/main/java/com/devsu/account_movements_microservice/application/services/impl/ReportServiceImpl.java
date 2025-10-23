package com.devsu.account_movements_microservice.application.services.impl;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.devsu.account_movements_microservice.application.dtos.in.QueryReportDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.ReportResponseDTO;
import com.devsu.account_movements_microservice.application.mappers.AccountMapper;
import com.devsu.account_movements_microservice.application.mappers.MovementMapper;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IMovementRepository;
import com.devsu.account_movements_microservice.application.services.IReportService;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Movement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final IAccountRepository accountRepository;
    private final IMovementRepository movementRepository;

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    private final MovementMapper movementMapper = Mappers.getMapper(MovementMapper.class);

    @Override
    public Mono<ReportResponseDTO> generateReport(QueryReportDTO query) {
        Mono<List<Account>> accounts = accountRepository.getByPropietaryId(query.getClientId());
        Mono<List<Movement>> movements = movementRepository.findByDatesAndPropietaryId(query);

        return Mono.zip(accounts, movements)
                .map(tuple -> {
                    ReportResponseDTO report = new ReportResponseDTO();
                    List<AccountResponseDTO> accountsDTO = tuple.getT1().stream()
                            .map(account -> {
                                AccountResponseDTO accountDTO = accountMapper.toAccountResponseDTO(account);
                                accountDTO.setPropietaryId(null);
                                return accountDTO;
                            })
                            .toList();
                    List<MovementResponseDTO> movementsDTO = tuple.getT2().stream()
                            .map(movement -> {
                                MovementResponseDTO movementDTO = movementMapper.toMovementResponseDTO(movement);
                                movementDTO.setAccount(null);
                                return movementDTO;
                            })
                            .toList();
                    report.setAccounts(accountsDTO);
                    report.setMovements(movementsDTO);
                    return report;
                });
    }

}
