package com.devsu.account_movements_microservice.application.dtos.in;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryReportDTO {

    @NotNull(message = "clientId is required")
    @Min(value = 1, message = "clientId must be greater than or equal to 1")
    private Long clientId;

    @NotNull(message = "startDate is required")
    @Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "startDate must be in format yyyy-MM-dd"
    )
    private String startDate;

    @NotNull(message = "endDate is required")
    @Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "endDate must be in format yyyy-MM-dd"
    )
    private String endDate;

    @AssertTrue(message = "endDate must be after or equal to startDate")
    public boolean isEndDateAfterStartDate() {
        try {
            if (startDate == null || endDate == null) return true;
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return !end.isBefore(start);
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}