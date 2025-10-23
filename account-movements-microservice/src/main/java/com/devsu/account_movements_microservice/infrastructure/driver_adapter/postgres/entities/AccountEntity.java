package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities;

import com.devsu.account_movements_microservice.domain.models.EAccountType;
import com.devsu.account_movements_microservice.domain.models.EState;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "accounts")
@AttributeOverride(name = "id", column = @Column(name = "account_id"))
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity extends AbstractEntity {
    private Long accountNumber;
    private Integer balance;
    @Enumerated(EnumType.STRING)
    private EState state;
    @Enumerated(EnumType.STRING)
    private EAccountType type;

    @Column(name = "propietary_id")
    private Long propietaryId;
}
