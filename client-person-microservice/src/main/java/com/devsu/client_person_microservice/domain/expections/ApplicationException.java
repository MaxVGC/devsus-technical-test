package com.devsu.client_person_microservice.domain.expections;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
}
