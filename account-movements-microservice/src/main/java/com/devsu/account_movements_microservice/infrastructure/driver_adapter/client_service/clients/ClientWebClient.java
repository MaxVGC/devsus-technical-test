package com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.clients;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${com.devsu.clients-service.url:http://localhost:8081}")
    private String url;

    private final WebClient webClient;
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    public Mono<Optional<Client>> getClientNameById(Long id) {
        return webClient.get()
                .uri(url.concat("/api/v1/clients/"+id))
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .map(clientMapper::toClient)
                .map(Optional::of)
                .onErrorReturn(Optional.empty());

    }

}
