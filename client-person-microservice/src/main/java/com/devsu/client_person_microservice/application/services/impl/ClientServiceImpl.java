package com.devsu.client_person_microservice.application.services.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devsu.client_person_microservice.application.dtos.in.CreateClientRequestDTO;
import com.devsu.client_person_microservice.application.dtos.out.ClientResponseDTO;
import com.devsu.client_person_microservice.application.mappers.ClientMapper;
import com.devsu.client_person_microservice.application.ports.IClientRepository;
import com.devsu.client_person_microservice.application.services.IClientService;
import com.devsu.client_person_microservice.domain.expections.ApplicationException;
import com.devsu.client_person_microservice.domain.model.Client;
import com.devsu.client_person_microservice.domain.model.EState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {
    private final IClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Override
    public Mono<Void> create(CreateClientRequestDTO request) {
        Client client = clientMapper.toClient(request);
        return clientRepository.findByIdentification(client.getIdentification())
                .flatMap(existingClientOpt -> {
                    if (existingClientOpt.isPresent()) {
                        return Mono.error(new ApplicationException("Client with identification already exists"));
                    } else {
                        client.setPassword(passwordEncoder.encode(client.getPassword()));
                        client.setState(EState.ACTIVE);
                        log.info("Creating client with identification: {}", client.getIdentification());
                        return clientRepository.save(client).flatMap(savedClient -> {
                            kafkaTemplate.send("new-account", savedClient.getId().toString());
                            log.info("Published new-account event for client ID: {}", savedClient.getId());
                            return Mono.empty();
                        });
                    }
                });
    }

    @Override
    public Mono<Void> update(Long id, CreateClientRequestDTO request) {
        return clientRepository.findById(id)
                .flatMap(existingClientOpt -> {
                    if (existingClientOpt.isPresent()) {
                        Client existingClient = existingClientOpt.get();
                        clientMapper.toClient(request);
                        log.info("Updating client with id: {}", id);
                        return clientRepository.save(existingClient).then();
                    } else {
                        return Mono.error(new ApplicationException("Client not found"));
                    }
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return clientRepository.findById(id)
                .flatMap(existingClientOpt -> {
                    if (existingClientOpt.isPresent()) {
                        // NOTE: We could implement a soft delete by changing the state instead of
                        // deleting the record
                        // existingClientOpt.get().setState(EState.INACTIVE);
                        // return clientRepository.save(existingClientOpt.get()).then();
                        log.info("Deleting client with id: {}", id);
                        return clientRepository.deleteById(id);
                    } else {
                        return Mono.error(new ApplicationException("Client not found"));
                    }
                });
    }

    @Override
    public Mono<ClientResponseDTO> getById(Long id) {
        return clientRepository.findById(id)
                .flatMap(existingClientOpt -> {
                    if (existingClientOpt.isPresent()) {
                        ClientResponseDTO response = clientMapper.toClientResponseDTO(existingClientOpt.get());
                        log.info("Retrieving client with id: {}", id);
                        return Mono.just(response);
                    } else {
                        return Mono.error(new ApplicationException("Client not found"));
                    }
                });
    }
}
