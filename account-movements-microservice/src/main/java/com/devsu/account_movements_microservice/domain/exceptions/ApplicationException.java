package com.devsu.account_movements_microservice.domain.exceptions;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
    
}
