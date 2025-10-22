package com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.devsu.client_person_microservice.application.ports.IClientRepository;
import com.devsu.client_person_microservice.domain.model.Client;
import com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.entities.ClientEntity;
import com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.mappers.ClientMapper;
import com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.repositories.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostgresAdapter implements IClientRepository {

    private final ClientRepository clientRepository;
    private final ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

    @Override
    public Mono<Client> save(Client client) {
        return Mono.fromCallable(() -> {
            ClientEntity clientEntity = mapper.toClientEntity(client);
            return mapper.toClient(clientRepository.save(clientEntity));
        });
    }

    @Override
    public Mono<Optional<Client>> findById(Long id) {
        return Mono.fromCallable(() -> {
            log.debug("Finding client by id: {}", id);
            Optional<ClientEntity> entityOpt = clientRepository.findById(id);
            return entityOpt.map(mapper::toClient);
        });
    }

    @Override
    public Mono<Optional<Client>> findByIdentification(String identification) {
        return Mono.fromCallable(() -> {
            log.debug("Finding client by identification: {}", identification);
            Optional<ClientEntity> entityOpt = clientRepository.findByIdentification(identification);
            return entityOpt.map(mapper::toClient);
        });
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromRunnable(() -> {
            log.debug("Deleting client by id: {}", id);
            clientRepository.deleteById(id);
        });
    }

}
