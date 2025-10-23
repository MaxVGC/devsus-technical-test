package com.devsu.account_movements_microservice.infrastructure.driver_adapter.client_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {
    private Long id;
    private String name;
    private String gender;
    private int age;
    private String address;
    private String phoneNumber;
    private String identification;
    private String state;
}
