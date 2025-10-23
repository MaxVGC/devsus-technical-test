package com.devsu.account_movements_microservice.infrastructure.driver_adapter.postgres.specifications;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

            if (params.getClientId() != null) {
                Join<MovementEntity, AccountEntity> j = root.join("account", JoinType.INNER);
                Long propietaryId = params.getClientId();
                preds.add(cb.equal(j.get("propietaryId"), propietaryId));
            }

            if (params.getStartDate() != null && params.getEndDate() != null) {
                try {
                    Date startDate = formatter.parse(params.getStartDate());
                    Date endDate = formatter.parse(params.getEndDate());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endDate);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    calendar.set(Calendar.MILLISECOND, 999);
                    endDate = calendar.getTime();

                    preds.add(cb.between(root.get("movementDate"), startDate, endDate));
                } catch (ParseException e) {
                    log.error("Error parsing date", e);
                }
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

}
