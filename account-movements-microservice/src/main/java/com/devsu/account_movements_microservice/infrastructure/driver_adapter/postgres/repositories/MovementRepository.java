package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.MovementEntity;

@Repository
public interface MovementRepository extends JpaRepository<MovementEntity, Long>, JpaSpecificationExecutor<MovementEntity> {
    
}
