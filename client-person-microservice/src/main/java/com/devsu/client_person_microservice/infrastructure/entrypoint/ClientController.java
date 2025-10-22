package com.devsu.client_person_microservice.infrastructure.entrypoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.client_person_microservice.application.dtos.in.CreateClientRequestDTO;
import com.devsu.client_person_microservice.application.dtos.out.ClientResponseDTO;
import com.devsu.client_person_microservice.application.services.IClientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Mono<Void>> create(@RequestBody @Valid CreateClientRequestDTO entity) {
        return ResponseEntity.ok(clientService.create(entity));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mono<ClientResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mono<Void>> update(@PathVariable Long id, @RequestBody @Valid CreateClientRequestDTO entity) {
        return ResponseEntity.ok(clientService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mono<Void>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.delete(id));
    }

}
