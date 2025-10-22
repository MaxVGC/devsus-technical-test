package com.devsu.client_person_microservice.application.services;

import com.devsu.client_person_microservice.application.dtos.in.CreateClientRequestDTO;
import com.devsu.client_person_microservice.application.dtos.out.ClientResponseDTO;

import reactor.core.publisher.Mono;

public interface IClientService {
    public Mono<Void> create(CreateClientRequestDTO request);
    public Mono<Void> update(Long id, CreateClientRequestDTO request);
    public Mono<Void> delete(Long id);
    public Mono<ClientResponseDTO> getById(Long id);
}
