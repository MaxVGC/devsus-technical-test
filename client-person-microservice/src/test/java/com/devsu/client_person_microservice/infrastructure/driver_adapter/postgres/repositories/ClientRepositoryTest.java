package com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.entities.ClientEntity;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    void saveClient_shouldSaveSuccessfully() {
        ClientEntity client = new ClientEntity();
        ClientEntity savedClient = clientRepository.save(client);
        assertNotNull(savedClient.getId());
    }
}
