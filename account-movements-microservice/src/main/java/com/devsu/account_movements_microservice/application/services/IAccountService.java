package com.devsu.account_movements_microservice.application.services;

import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.in.UpdateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;

import reactor.core.publisher.Mono;

public interface IAccountService {
    Mono<Void> create(CreateAccountRequestDTO dto);
    Mono<Void> update(Long id, UpdateAccountRequestDTO dto);
    Mono<AccountResponseDTO> get(Long id);
}
