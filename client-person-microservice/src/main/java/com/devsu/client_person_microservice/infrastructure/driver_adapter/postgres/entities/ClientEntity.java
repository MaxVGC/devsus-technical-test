package com.devsu.client_person_microservice.infrastructure.driver_adapter.postgres.entities;

import com.devsu.client_person_microservice.domain.model.EState;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "person_id")
@Table(name = "clients")
@SuperBuilder
public class ClientEntity extends PersonEntity {
    private String password;
    @Enumerated(EnumType.STRING)
    private EState state;
}
