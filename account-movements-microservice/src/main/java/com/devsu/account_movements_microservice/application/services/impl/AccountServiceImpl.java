package com.devsu.account_movements_microservice.application.services.impl;

import java.util.List;
import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.in.UpdateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.application.mappers.AccountMapper;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IClientRepository;
import com.devsu.account_movements_microservice.application.services.IAccountService;
import com.devsu.account_movements_microservice.domain.exceptions.ApplicationException;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Client;
import com.devsu.account_movements_microservice.domain.models.EAccountType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;
    private final IClientRepository clientRepository;
    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Override
    public Mono<Void> create(CreateAccountRequestDTO dto) {
        Mono<Client> clientMono = clientRepository.findById(dto.getPropietaryId())
                .flatMap(optionalClient -> optionalClient
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new ApplicationException("Client not found"))));

        Mono<List<Account>> accountsMono = accountRepository.getByPropietaryId(dto.getPropietaryId());

        return Mono.zip(clientMono, accountsMono)
                .flatMap(tuple -> {
                    Client client = tuple.getT1();
                    List<Account> existingAccounts = tuple.getT2();

                    boolean hasSavings = existingAccounts.stream()
                            .anyMatch(acc -> acc.getType() == EAccountType.SAVINGS);
                    boolean hasChecking = existingAccounts.stream()
                            .anyMatch(acc -> acc.getType() == EAccountType.CHECKING);

                    if (dto.getType() == EAccountType.SAVINGS && hasSavings) {
                        return Mono.error(new ApplicationException("Client already has a SAVINGS account"));
                    }
                    if (dto.getType() == EAccountType.CHECKING && hasChecking) {
                        return Mono.error(new ApplicationException("Client already has a CHECKING account"));
                    }

                    Account account = accountMapper.toAccount(dto, client);
                    return accountRepository.save(account).then();
                });
    }

    @Override
    public Mono<Void> update(Long id, UpdateAccountRequestDTO dto) {
        Mono<Account> accountMono = accountRepository.findById(id)
                .flatMap(optionalAccount -> optionalAccount
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new ApplicationException("Account not found"))));

        Mono<List<Account>> accountsMono = accountMono
                .flatMap(account -> accountRepository.getByPropietaryId(account.getPropietary().getId()));

        return Mono.zip(accountMono, accountsMono)
                .flatMap(tuple -> {
                    Account account = tuple.getT1();
                    List<Account> existingAccounts = tuple.getT2();

                    boolean hasSavings = existingAccounts.stream()
                            .anyMatch(acc -> acc.getType() == EAccountType.SAVINGS && !acc.getId().equals(id));
                    boolean hasChecking = existingAccounts.stream()
                            .anyMatch(acc -> acc.getType() == EAccountType.CHECKING && !acc.getId().equals(id));

                    if (dto.getType() == EAccountType.SAVINGS && hasSavings) {
                        return Mono.error(new ApplicationException("Client already has a SAVINGS account"));
                    }
                    if (dto.getType() == EAccountType.CHECKING && hasChecking) {
                        return Mono.error(new ApplicationException("Client already has a CHECKING account"));
                    }

                    account.setState(dto.getState());
                    account.setType(dto.getType());
                    account.setAccountNumber(dto.getAccountNumber());

                    return accountRepository.save(account).then();
                });
    }

    @Override
    public Mono<AccountResponseDTO> get(Long id) {
        return accountRepository.findById(id)
                .flatMap(optionalAccount -> optionalAccount
                        .map(account -> Mono.just(accountMapper.toAccountResponseDTO(account)))
                        .orElseGet(() -> Mono.error(new ApplicationException("Account not found"))));
    }

}
