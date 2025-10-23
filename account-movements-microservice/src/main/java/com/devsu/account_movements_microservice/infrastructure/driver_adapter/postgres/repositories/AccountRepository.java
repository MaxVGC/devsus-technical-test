package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByPropietaryId(Long propietaryId);
    Optional<AccountEntity> findByAccountNumber(Long accountNumber);
}
