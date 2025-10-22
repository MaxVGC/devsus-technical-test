package com.devsu.account_movements_microservice.application.ports;

import java.util.Optional;

import com.devsu.account_movements_microservice.domain.models.Movement;

import reactor.core.publisher.Mono;

public interface IMovementRepository {
    public Mono<Movement> save(Movement movement);
    public Mono<Optional<Movement>> findById(Long id);
}
