package com.devsu.account_movements_microservice.application.ports;

import java.util.Optional;

import com.devsu.account_movements_microservice.domain.models.Client;

import reactor.core.publisher.Mono;

public interface IClientRepository {
    public Mono<Optional<Client>> findById(Long id);
}
