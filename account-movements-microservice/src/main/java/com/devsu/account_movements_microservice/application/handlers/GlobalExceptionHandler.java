package com.devsu.account_movements_microservice.application.handlers;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import com.devsu.account_movements_microservice.application.dtos.out.ErrorResponseDTO;
import com.devsu.account_movements_microservice.domain.exceptions.ApplicationException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> exceptionHandler(Exception exception) {
        log.error("General exception", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .message("An unexpected error occurred on the server")
                        .code("500")
                        .build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> noResourceFoundExceptionHandler(NoResourceFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .message("The requested resource was not found")
                        .code("404")
                        .build());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDTO> applicationExceptionHandler(ApplicationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .code("400")
                        .build());
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponseDTO> customValidationErrorHandling(
            WebExchangeBindException exception) {

        String exceptionMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError())
                .getDefaultMessage();
        log.error("Validation exception: {}", exceptionMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(exceptionMessage)
                        .code("400")
                        .build());
    }

}