package com.devsu.account_movements_microservice.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.devsu.account_movements_microservice.application.dtos.in.CreateMovementRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.domain.models.Account;
import com.devsu.account_movements_microservice.domain.models.EMovementType;
import com.devsu.account_movements_microservice.domain.models.Movement;

@Mapper
public interface MovementMapper {
    @Mapping(target = "account", source = "account")
    @Mapping(target = "type", source = "dto", qualifiedByName = "mapType")
    public Movement toMovement(CreateMovementRequestDTO dto, Account account);

    public MovementResponseDTO toMovementResponseDTO(Movement movement);

    @Named("mapType")
    default EMovementType mapType(CreateMovementRequestDTO dto) {
        if (dto.getAmount() > 0) {
            return EMovementType.DEPOSIT;
        } else {
            return EMovementType.WITHDRAWAL;
        }
    }
}
