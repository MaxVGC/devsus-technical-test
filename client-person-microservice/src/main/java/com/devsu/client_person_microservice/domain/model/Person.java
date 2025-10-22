package com.devsu.client_person_microservice.domain.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Person {
    private Long id;
    private String name;
    private EGender gender;
    private int age;
    private String address;
    private String phoneNumber;
    private String identification;
}
