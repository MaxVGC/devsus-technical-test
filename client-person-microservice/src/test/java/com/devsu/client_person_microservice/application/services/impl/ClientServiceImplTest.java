package com.devsu.client_person_microservice.application.services.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import com.devsu.client_person_microservice.application.dtos.in.CreateClientRequestDTO;
import com.devsu.client_person_microservice.application.dtos.out.ClientResponseDTO;
import com.devsu.client_person_microservice.application.mappers.ClientMapper;
import com.devsu.client_person_microservice.application.ports.IClientRepository;
import com.devsu.client_person_microservice.domain.expections.ApplicationException;
import com.devsu.client_person_microservice.domain.model.Client;
import com.devsu.client_person_microservice.domain.model.EState;
import org.junit.jupiter.api.extension.ExtendWith;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private IClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ClientMapper clientMapper;

    @Captor
    private ArgumentCaptor<Client> clientCaptor;

    private ClientServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        service = new ClientServiceImpl(clientRepository, passwordEncoder, kafkaTemplate);
        Field clientMapperField = ClientServiceImpl.class.getDeclaredField("clientMapper");
        clientMapperField.setAccessible(true);
        clientMapperField.set(service, clientMapper);
    }

    @Test
    void create_success_publishesEvent_and_savesEncodedPassword_and_activeState() {
        CreateClientRequestDTO request = new CreateClientRequestDTO();
        Client mappedClient = new Client();
        mappedClient.setIdentification("ID123");
        mappedClient.setPassword("rawPass");

        when(clientMapper.toClient(request)).thenReturn(mappedClient);
        when(clientRepository.findByIdentification("ID123")).thenReturn(Mono.just(Optional.empty()));
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");

        Client savedClient = new Client();
        savedClient.setId(1L);
        savedClient.setIdentification("ID123");
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(savedClient));

        StepVerifier.create(service.create(request)).verifyComplete();

        verify(clientRepository).findByIdentification("ID123");
        verify(passwordEncoder).encode("rawPass");
        verify(clientRepository).save(clientCaptor.capture());
        Client toSave = clientCaptor.getValue();
        assert toSave.getIdentification().equals("ID123");
        assert toSave.getPassword().equals("encodedPass");
        assert toSave.getState() == EState.ACTIVE;
        verify(kafkaTemplate).send("new-account", "1");
    }

    @Test
    void create_whenIdentificationExists_emitsApplicationException() {
        CreateClientRequestDTO request = new CreateClientRequestDTO();
        Client mappedClient = new Client();
        mappedClient.setIdentification("ID_EXISTS");
        mappedClient.setPassword("p");

        when(clientMapper.toClient(request)).thenReturn(mappedClient);
        when(clientRepository.findByIdentification("ID_EXISTS"))
                .thenReturn(Mono.just(Optional.of(new Client())));

        StepVerifier.create(service.create(request))
                .expectErrorMatches(throwable -> throwable instanceof ApplicationException
                        && throwable.getMessage().contains("already exists"))
                .verify();

        verify(clientRepository).findByIdentification("ID_EXISTS");
        verify(clientRepository, never()).save(any());
    }

    @Test
    void update_success_savesUpdatedClient() {
        Long id = 10L;
        CreateClientRequestDTO request = new CreateClientRequestDTO();
        Client existing = new Client();
        existing.setId(id);
        existing.setIdentification("ID_UPD");

        when(clientRepository.findById(id)).thenReturn(Mono.just(Optional.of(existing)));
        when(clientMapper.toClient(request)).thenReturn(new Client());
        when(clientRepository.save(existing)).thenReturn(Mono.just(existing));

        StepVerifier.create(service.update(id, request)).verifyComplete();

        verify(clientRepository).findById(id);
        verify(clientMapper).toClient(request);
        verify(clientRepository).save(existing);
    }

    @Test
    void update_notFound_emitsApplicationException() {
        Long id = 99L;
        CreateClientRequestDTO request = new CreateClientRequestDTO();

        when(clientRepository.findById(id)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.update(id, request))
                .expectErrorMatches(throwable -> throwable instanceof ApplicationException
                        && throwable.getMessage().contains("Client not found"))
                .verify();

        verify(clientRepository).findById(id);
        verify(clientRepository, never()).save(any());
    }

    @Test
    void delete_success_deletesById() {
        Long id = 5L;
        Client existing = new Client();
        existing.setId(id);

        when(clientRepository.findById(id)).thenReturn(Mono.just(Optional.of(existing)));
        when(clientRepository.deleteById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(id)).verifyComplete();

        verify(clientRepository).findById(id);
        verify(clientRepository).deleteById(id);
    }

    @Test
    void delete_notFound_emitsApplicationException() {
        Long id = 6L;
        when(clientRepository.findById(id)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.delete(id))
                .expectErrorMatches(throwable -> throwable instanceof ApplicationException
                        && throwable.getMessage().contains("Client not found"))
                .verify();

        verify(clientRepository).findById(id);
        verify(clientRepository, never()).deleteById(anyLong());
    }

    @Test
    void getById_success_returnsResponseDto() {
        Long id = 7L;
        Client existing = new Client();
        existing.setId(id);
        existing.setIdentification("ID_GET");

        ClientResponseDTO dto = new ClientResponseDTO();
        when(clientRepository.findById(id)).thenReturn(Mono.just(Optional.of(existing)));
        when(clientMapper.toClientResponseDTO(existing)).thenReturn(dto);

        StepVerifier.create(service.getById(id))
                .expectNextMatches(resp -> resp == dto)
                .verifyComplete();

        verify(clientRepository).findById(id);
        verify(clientMapper).toClientResponseDTO(existing);
    }

    @Test
    void getById_notFound_emitsApplicationException() {
        Long id = 8L;
        when(clientRepository.findById(id)).thenReturn(Mono.just(Optional.empty()));

        StepVerifier.create(service.getById(id))
                .expectErrorMatches(throwable -> throwable instanceof ApplicationException
                        && throwable.getMessage().contains("Client not found"))
                .verify();

        verify(clientRepository).findById(id);
        verify(clientMapper, never()).toClientResponseDTO(any());
    }
}