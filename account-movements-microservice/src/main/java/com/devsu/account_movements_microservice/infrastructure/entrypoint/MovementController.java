package com.devsu.account_movements_microservice.infrastructure.entrypoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.account_movements_microservice.application.services.IMovementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
public class MovementController {
    private final IMovementService movementService;
    
}
