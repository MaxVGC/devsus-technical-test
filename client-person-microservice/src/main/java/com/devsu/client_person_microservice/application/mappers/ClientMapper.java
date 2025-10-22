package com.devsu.client_person_microservice.application.mappers;

import org.mapstruct.Mapper;

import com.devsu.client_person_microservice.application.dtos.in.CreateClientRequestDTO;
import com.devsu.client_person_microservice.application.dtos.out.ClientResponseDTO;
import com.devsu.client_person_microservice.domain.model.Client;

@Mapper
public interface ClientMapper {
    public Client toClient(CreateClientRequestDTO dto);
    public ClientResponseDTO toClientResponseDTO(Client client);
}
