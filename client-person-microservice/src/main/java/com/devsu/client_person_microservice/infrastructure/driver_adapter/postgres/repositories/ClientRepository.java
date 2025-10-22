package com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.entities.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByIdentification(String identification);
}
