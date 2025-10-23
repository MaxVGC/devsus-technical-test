package com.devsu.account_movements_microservice.infrastructure.entrypoint;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.devsu.account_movements_microservice.application.dtos.in.CreateAccountRequestDTO;
import com.devsu.account_movements_microservice.application.services.IAccountService;
import com.devsu.account_movements_microservice.domain.models.EAccountType;

import io.netty.util.internal.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountEvents {

    private final IAccountService accountService;

    @KafkaListener(topics = "new-account", groupId = "account-service-group")
    public void newAccount(String message) {
        log.info("Received message on 'new-account': {}", message);

        Long propietaryId;
        try {
            propietaryId = Long.parseLong(message.trim());
        } catch (NumberFormatException e) {
            log.error("Invalid message received in new-account topic: {}", message, e);
            return;
        }

        String accountNumberStr = String.format("%016d",
                ThreadLocalRandom.current().nextLong(1_0000_0000_0000_000L, 9_9999_9999_9999_999L));

        CreateAccountRequestDTO newAccount = CreateAccountRequestDTO.builder()
                .accountNumber(Long.parseLong(accountNumberStr))
                .balance(0)
                .propietaryId(propietaryId)
                .type("SAVINGS")
                .build();

        accountService.createByEvent(newAccount)
                .doOnSubscribe(sub -> log.info("Creating new account for client ID: {}", propietaryId))
                .doOnSuccess(unused -> log.info("Account created successfully for client {}", propietaryId))
                .doOnError(error -> log.error("Failed to create account for client {}: {}", propietaryId,
                        error.getMessage()))
                .subscribe();
    }

}
