package com.devsu.client_person_microservice.application.ports;

import java.util.Optional;

import com.devsu.client_person_microservice.domain.model.Client;

import reactor.core.publisher.Mono;

public interface IClientRepository {
    Mono<Client> save(Client client);

    Mono<Optional<Client>> findById(Long id);

    Mono<Optional<Client>> findByIdentification(String identification);

    Mono<Void> deleteById(Long id);
}
