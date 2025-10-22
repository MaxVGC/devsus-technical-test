package com.devsu.client_person_microservice.domain.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Client extends Person {
    private String password;
    private EState state;
}
