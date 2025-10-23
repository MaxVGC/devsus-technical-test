package com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.devsu.account_movements_microservice.application.ports.IClientRepository;
import com.devsu.account_movements_microservice.domain.models.Client;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.clients.ClientWebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientAdapter implements IClientRepository {

    private final ClientWebClient clientWebClient;

    @Override
    public Mono<Optional<Client>> findById(Long id) {
        return clientWebClient.getClientNameById(id);
    }

}
