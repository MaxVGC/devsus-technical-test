package com.devsu.account_movements_microservice.domain.models;

import lombok.Data;

@Data
public class Client {
    private Long id;
    private String name;
    private EGender gender;
    private int age;
    private String address;
    private String phoneNumber;
    private String identification;
    private EState state;
}
