package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsu.account_movements_microservice.domain.models.Movement;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.MovementEntity;

@Mapper
public interface MovementMapper {
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "account", source = "dto.account")
    Movement toMovement(MovementEntity dto);
    MovementEntity toMovementEntity(Movement movement);
}
