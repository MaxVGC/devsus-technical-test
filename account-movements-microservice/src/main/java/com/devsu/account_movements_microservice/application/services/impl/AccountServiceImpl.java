package com.devsu.account_movements_microservice.application.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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
import com.devsu.account_movements_microservice.domain.models.EState;

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
                                                .orElseGet(() -> Mono
                                                                .error(new ApplicationException("Client not found"))));

                Mono<List<Account>> accountsMono = accountRepository.getByPropietaryId(dto.getPropietaryId());

                Mono<Void> uniqueAccountCheckMono = accountRepository.findByAccountNumber(dto.getAccountNumber())
                                .flatMap(optionalAccount -> {
                                        if (optionalAccount.isPresent()) {
                                                return Mono.error(new ApplicationException(
                                                                "Account number already exists"));
                                        }
                                        return Mono.empty();
                                });

                return Mono.zip(clientMono, accountsMono)
                                .flatMap(tuple -> {
                                        List<Account> existingAccounts = tuple.getT2();

                                        boolean hasSavings = existingAccounts.stream()
                                                        .anyMatch(acc -> acc.getType() == EAccountType.SAVINGS);
                                        boolean hasChecking = existingAccounts.stream()
                                                        .anyMatch(acc -> acc.getType() == EAccountType.CHECKING);

                                        EAccountType dtoType = dto.getType() != null
                                                        ? EAccountType.valueOf(dto.getType())
                                                        : null;

                                        if (dtoType == EAccountType.SAVINGS && hasSavings) {
                                                return Mono.error(new ApplicationException(
                                                                "Client already has a SAVINGS account"));
                                        }
                                        if (dtoType == EAccountType.CHECKING && hasChecking) {
                                                return Mono.error(new ApplicationException(
                                                                "Client already has a CHECKING account"));
                                        }

                                        Account account = accountMapper.toAccount(dto, dto.getPropietaryId());
                                        account.setState(EState.ACTIVE);

                                        return uniqueAccountCheckMono.then(accountRepository.save(account)).then();
                                });
        }

        @Override
        public Mono<Void> createByEvent(CreateAccountRequestDTO dto) {
                return tryCreateAccount(dto, 5);
        }

        private Mono<Void> tryCreateAccount(CreateAccountRequestDTO dto, int remainingRetries) {
                Account account = accountMapper.toAccount(dto, dto.getPropietaryId());
                account.setState(EState.ACTIVE);

                long accountNumber = ThreadLocalRandom.current().nextLong(1_0000_0000_0000_000L, 9_9999_9999_9999_999L);
                account.setAccountNumber(accountNumber);

                return accountRepository.findByAccountNumber(accountNumber)
                                .flatMap(optionalAccount -> {
                                        if (optionalAccount.isPresent()) {
                                                if (remainingRetries > 0) {
                                                        log.warn("⚠️ Account number {} already exists, retrying... ({} retries left)",
                                                                        accountNumber, remainingRetries - 1);
                                                        return tryCreateAccount(dto, remainingRetries - 1);
                                                } else {
                                                        return Mono.error(new ApplicationException(
                                                                        "Failed to generate unique account number"));
                                                }
                                        }
                                        return accountRepository.save(account).then();
                                })
                                .switchIfEmpty(accountRepository.save(account).then());
        }

        @Override
        public Mono<Void> update(Long id, UpdateAccountRequestDTO dto) {
                Mono<Account> accountMono = accountRepository.findAccountById(id)
                                .flatMap(optionalAccount -> optionalAccount
                                                .map(Mono::just)
                                                .orElseGet(() -> Mono
                                                                .error(new ApplicationException("Account not found"))));

                Mono<List<Account>> accountsMono = accountMono
                                .flatMap(account -> accountRepository.getByPropietaryId(account.getPropietaryId()));

                return Mono.zip(accountMono, accountsMono)
                                .flatMap(tuple -> {
                                        Account account = tuple.getT1();
                                        List<Account> existingAccounts = tuple.getT2();

                                        boolean hasSavings = existingAccounts.stream()
                                                        .anyMatch(acc -> acc.getType() == EAccountType.SAVINGS
                                                                        && !acc.getId().equals(id));
                                        boolean hasChecking = existingAccounts.stream()
                                                        .anyMatch(acc -> acc.getType() == EAccountType.CHECKING
                                                                        && !acc.getId().equals(id));

                                        EAccountType dtoType = dto.getType() != null
                                                        ? EAccountType.valueOf(dto.getType())
                                                        : null;

                                        if (dtoType == EAccountType.SAVINGS && hasSavings) {
                                                return Mono.error(new ApplicationException(
                                                                "Client already has a SAVINGS account"));
                                        }
                                        if (dtoType == EAccountType.CHECKING && hasChecking) {
                                                return Mono.error(new ApplicationException(
                                                                "Client already has a CHECKING account"));
                                        }

                                        account.setState(EState.valueOf(dto.getState()));
                                        account.setType(dtoType);
                                        account.setAccountNumber(dto.getAccountNumber());

                                        return accountRepository.save(account).then();
                                });
        }

        @Override
        public Mono<AccountResponseDTO> get(Long id) {
                return accountRepository.findAccountById(id)
                                .flatMap(optionalAccount -> optionalAccount
                                                .map(account -> Mono.just(accountMapper.toAccountResponseDTO(account)))
                                                .orElseGet(() -> Mono
                                                                .error(new ApplicationException("Account not found"))));
        }

}
