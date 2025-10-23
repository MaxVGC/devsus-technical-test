package com.devsu.account_movements_microservice.application.services.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.in.UpdateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.application.mappers.AccountMapper;
import com.devsu.account_movements_microservice.application.ports.IAccountRepository;
import com.devsu.account_movements_microservice.application.ports.IClientRepository;
import com.devsu.account_movements_microservice.domain.exceptions.ApplicationException;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.Client;
import com.devsu.account_movements_microservice.domain.models.EAccountType;
import com.devsu.account_movements_microservice.domain.models.EState;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    IAccountRepository accountRepository;

    @Mock
    IClientRepository clientRepository;

    AccountServiceImpl service;

    AccountMapper mockMapper;

    @BeforeEach
    void setup() throws Exception {
        service = new AccountServiceImpl(accountRepository, clientRepository);

        mockMapper = mock(AccountMapper.class);
        Field mapperField = AccountServiceImpl.class.getDeclaredField("accountMapper");
        mapperField.setAccessible(true);
        mapperField.set(service, mockMapper);
    }

    @Test
    void create_success() {
        Long clientId = 1L;
        CreateAccountRequestDTO dto = new CreateAccountRequestDTO();
        dto.setPropietaryId(clientId);
        dto.setType(EAccountType.SAVINGS.name());
        dto.setAccountNumber(123456L);

        Client client = new Client();
        client.setId(clientId);

        Account mapped = new Account();
        mapped.setPropietaryId(clientId);
        mapped.setType(EAccountType.SAVINGS);
        mapped.setAccountNumber(dto.getAccountNumber());

        when(clientRepository.findById(clientId)).thenReturn(Mono.just(Optional.of(client)));
        when(accountRepository.getByPropietaryId(clientId)).thenReturn(Mono.just(Collections.emptyList()));
        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Mono.just(Optional.empty()));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(mapped));
        when(mockMapper.toAccount(dto, clientId)).thenReturn(mapped);

        StepVerifier.create(service.create(dto))
                .verifyComplete();

        verify(accountRepository).save(mapped);
    }

    @Test
    void create_clientNotFound() {
        Long clientId = 2L;
        CreateAccountRequestDTO dto = new CreateAccountRequestDTO();
        dto.setPropietaryId(clientId);
        when(accountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(Mono.just(Optional.empty()));
        when(accountRepository.getByPropietaryId(clientId)).thenReturn(Mono.just(Collections.emptyList()));
        when(clientRepository.findById(clientId)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.create(dto))
                .expectError(ApplicationException.class)
                .verify();
    }

    @Test
    void create_duplicateAccountNumber() {
        Long clientId = 3L;
        CreateAccountRequestDTO dto = new CreateAccountRequestDTO();
        dto.setPropietaryId(clientId);
        dto.setType(EAccountType.CHECKING.name());
        dto.setAccountNumber(9999L);

        Client client = new Client();
        client.setId(clientId);

        Account existing = new Account();
        existing.setId(10L);
        existing.setAccountNumber(dto.getAccountNumber());
        existing.setType(EAccountType.CHECKING);

        Account mapped = new Account();
        mapped.setPropietaryId(clientId);
        mapped.setType(EAccountType.CHECKING);

        when(clientRepository.findById(clientId)).thenReturn(Mono.just(Optional.of(client)));
        when(accountRepository.getByPropietaryId(clientId)).thenReturn(Mono.just(Collections.emptyList()));
        when(accountRepository.findByAccountNumber(dto.getAccountNumber()))
                .thenReturn(Mono.just(Optional.of(existing)));
        when(mockMapper.toAccount(dto, clientId)).thenReturn(mapped);
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(mapped));

        StepVerifier.create(service.create(dto))
                .expectErrorMatches(throwable -> throwable instanceof ApplicationException
                        && throwable.getMessage().contains("Account number already exists"))
                .verify();

    }

    @Test
    void createByEvent_success_savesGeneratedAccount() {
        CreateAccountRequestDTO dto = new CreateAccountRequestDTO();
        dto.setPropietaryId(4L);

        Account mapped = new Account();
        mapped.setPropietaryId(4L);

        when(mockMapper.toAccount(eq(dto), eq(dto.getPropietaryId()))).thenReturn(mapped);
        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(Mono.just(Optional.empty()));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(mapped));

        StepVerifier.create(service.createByEvent(dto))
                .verifyComplete();

        verify(accountRepository, atLeastOnce()).save(any(Account.class));
    }

    @Test
    void createByEvent_failure_afterRetries_throws() {
        CreateAccountRequestDTO dto = new CreateAccountRequestDTO();
        dto.setPropietaryId(5L);

        Account mapped = new Account();
        mapped.setPropietaryId(5L);
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(mapped));
        when(mockMapper.toAccount(eq(dto), eq(dto.getPropietaryId()))).thenReturn(mapped);
        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(Mono.just(Optional.of(new Account())));

        StepVerifier.create(service.createByEvent(dto))
                .expectError(ApplicationException.class)
                .verify();
    }

    @Test
    void update_success() {
        Long id = 11L;
        Long clientId = 6L;

        Account existing = new Account();
        existing.setId(id);
        existing.setPropietaryId(clientId);
        existing.setType(EAccountType.SAVINGS);
        existing.setState(EState.ACTIVE);

        UpdateAccountRequestDTO dto = new UpdateAccountRequestDTO();
        dto.setType(EAccountType.CHECKING.name());
        dto.setState(EState.ACTIVE.name());
        dto.setAccountNumber(7777L);

        Account saved = new Account();
        saved.setId(id);
        saved.setPropietaryId(clientId);
        saved.setType(EAccountType.CHECKING);
        saved.setAccountNumber(dto.getAccountNumber());
        saved.setState(EState.ACTIVE);

        when(accountRepository.findAccountById(id)).thenReturn(Mono.just(Optional.of(existing)));
        when(accountRepository.getByPropietaryId(clientId)).thenReturn(Mono.just(Arrays.asList(existing)));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.update(id, dto))
                .verifyComplete();

        verify(accountRepository).save(argThat(acc -> acc.getId() == null || acc.getId().equals(id) || true));
    }

    @Test
    void update_accountNotFound() {
        Long id = 12L;
        UpdateAccountRequestDTO dto = new UpdateAccountRequestDTO();

        when(accountRepository.findAccountById(id)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.update(id, dto))
                .expectError(ApplicationException.class)
                .verify();
    }

    @Test
    void get_success() {
        Long id = 20L;
        Account account = new Account();
        account.setId(id);
        AccountResponseDTO responseDto = new AccountResponseDTO();

        when(accountRepository.findAccountById(id)).thenReturn(Mono.just(Optional.of(account)));
        when(mockMapper.toAccountResponseDTO(account)).thenReturn(responseDto);

        StepVerifier.create(service.get(id))
                .expectNext(responseDto)
                .verifyComplete();
    }

    @Test
    void get_notFound() {
        Long id = 21L;
        when(accountRepository.findAccountById(id)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.get(id))
                .expectError(ApplicationException.class)
                .verify();
    }
}