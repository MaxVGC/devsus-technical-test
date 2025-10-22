package com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.mappers;

import org.mapstruct.Mapper;

import com.devsu.client_person_microservice.domain.model.Client;
import com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.entities.ClientEntity;

@Mapper
public interface ClientMapper {
    ClientEntity toClientEntity(Client client);
    Client toClient(ClientEntity entity);
}
