package com.devsu.account_movements_microservice.application.services;

import com.devsu.account_movements_microservice.application.dtos.in.CreateMovementRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.TransactionResponseDTO;

import reactor.core.publisher.Mono;

public interface IMovementService {
    public Mono<TransactionResponseDTO> create(CreateMovementRequestDTO dto);
    public Mono<MovementResponseDTO> get(Long id);
}
