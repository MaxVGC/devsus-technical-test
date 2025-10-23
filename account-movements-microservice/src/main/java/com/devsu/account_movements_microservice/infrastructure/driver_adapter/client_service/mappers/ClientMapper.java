package com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.mappers;

import org.mapstruct.Mapper;

import com.devsu.account_movements_microservice.domain.models.Client;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.dtos.ClientDTO;

@Mapper
public interface ClientMapper {
    Client toClient(ClientDTO dto);
}
