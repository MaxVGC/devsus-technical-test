package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.specifications;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.domain.Specification;

import com.devsu.account_movements_microservice.application.dtos.in.QueryReportDTO;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.AccountEntity;
import com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.entities.MovementEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Slf4j
public class ReportSpecification {
     public static Specification<MovementEntity> withFilters(QueryReportDTO params) {
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT-5"));

            if(params.getClientId() != null) {
                Join<MovementEntity, AccountEntity> j = root.join("account", JoinType.INNER);
                preds.add(cb.equal(j.get("clientId"), params.getClientId()));
            }

            if (params.getStartDate() != null && params.getEndDate() != null) {
                params.setStartDate(params.getStartDate().replace("%20", " "));
                params.setEndDate(params.getEndDate().replace("%20", " "));
                try {
                    preds.add(cb.between(root.get("movementDate"), formatter.parse(params.getStartDate()), formatter.parse(params.getEndDate())));
                } catch (ParseException e) {
                    log.error("Error parsing date", e);
                }
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

}
