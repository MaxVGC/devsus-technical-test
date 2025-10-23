package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities;

import java.util.Date;

import com.devsu.account_movements_microservice.domain.models.EMovementType;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "movements")
@AttributeOverride(name = "id", column = @Column(name = "movement_id"))
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MovementEntity extends AbstractEntity {
    private Date movementDate;
    @Enumerated(EnumType.STRING)
    private EMovementType type;
    private Integer amount;
    private Integer balance;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

}
