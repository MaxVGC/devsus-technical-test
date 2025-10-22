package com.devsu.client_person_microservice.application.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import com.devsu.client_person_microservice.application.dtos.out.ErrorResponseDTO;

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
                        .message("message.error.general")
                        .code("500")
                        .build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> noResourceFoundExceptionHandler(NoResourceFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .message("message.error.resource.notFound")
                        .code("404")
                        .build());
    }

}
