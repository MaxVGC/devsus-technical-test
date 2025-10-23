package com.devsu.account_movements_microservice.infrastructure.entrypoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.account_movements_microservice.application.dtos.in.CreateMovementRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.MovementResponseDTO;
import com.devsu.account_movements_microservice.application.dtos.out.TransactionResponseDTO;
import com.devsu.account_movements_microservice.application.services.IMovementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController {
    private final IMovementService movementService;

    @GetMapping("/{id}")
    public ResponseEntity<Mono<MovementResponseDTO>> getMovementById(@PathVariable Long id) {
        return ResponseEntity.ok(movementService.get(id));
    }

    @PostMapping
    public ResponseEntity<Mono<TransactionResponseDTO>> createMovement(@RequestBody @Valid CreateMovementRequestDTO movementRequest) {
        return ResponseEntity.ok(movementService.create(movementRequest));
    }
    
}
