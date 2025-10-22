package com.devsu.client_person_microservice.application.dtos.out;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String gender;
    private int age;
    private String address;
    private String phoneNumber;
    private String identification;
    private String state;
}
