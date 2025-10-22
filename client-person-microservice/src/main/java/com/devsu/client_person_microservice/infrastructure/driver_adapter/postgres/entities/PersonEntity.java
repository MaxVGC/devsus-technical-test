package com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.entities;

import com.devsu.client_person_microservice.domain.model.EGender;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "people")
@AttributeOverride(name = "id", column = @Column(name = "person_id"))
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity extends AbstractEntity {
    private String name;
    @Enumerated(EnumType.STRING)
    private EGender gender;
    private int age;
    private String address;
    private String phoneNumber;
    private String identification;
}
