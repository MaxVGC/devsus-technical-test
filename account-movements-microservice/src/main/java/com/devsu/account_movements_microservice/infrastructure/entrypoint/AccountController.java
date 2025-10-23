package com.devsu.account_movements_microservice.infrastructure.entrypoint;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.in.UpdateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.dtos.out.AccountResponseDTO;
import com.devsu.account_movements_microservice.application.services.IAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<Mono<AccountResponseDTO>> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.get(id));
    }

    @PostMapping
    public ResponseEntity<Mono<Void>> createAccount(@RequestBody @Valid CreateAccountRequestDTO accountRequest) {
        return ResponseEntity.ok(accountService.create(accountRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<Void>> updateAccount(@PathVariable Long id,
            @RequestBody @Valid UpdateAccountRequestDTO accountRequest) {
        return ResponseEntity.ok(accountService.update(id, accountRequest));
    }
}
