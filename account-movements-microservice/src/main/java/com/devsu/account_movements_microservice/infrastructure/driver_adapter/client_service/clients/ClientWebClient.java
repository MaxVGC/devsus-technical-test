package com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.clients;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.devsu.account_movements_microservice.domain.models.Client;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.dtos.ClientDTO;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.mappers.ClientMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClientWebClient {

    private final WebClient webClient;
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    public Mono<Optional<Client>> getClientNameById(Long id) {
        return webClient.get()
                .uri("/api/v1/clients/{id}", id)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .map(clientMapper::toClient)
                .map(Optional::of)
                .onErrorReturn(Optional.empty());

    }

}
