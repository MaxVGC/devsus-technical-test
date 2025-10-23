package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.MovementEntity;

@DataJpaTest
class MovementRepositoryTest {

    @Autowired
    MovementRepository movementRepository;

    @Test
    void saveMovement_shouldSaveSuccessfully() {
        MovementEntity movement = new MovementEntity();
        movement.setMovementDate(Date.from(Instant.now()));
        MovementEntity savedMovement = movementRepository.save(movement);
        assertNotNull(savedMovement.getId());
    }

}
