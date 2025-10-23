package com.devsu.account_movements_microservice.application.services;

import com.devsu.account_movements_microservice.application.dtos.in.QueryReportDTO;
import com.devsu.account_movements_microservice.application.dtos.out.ReportResponseDTO;

import reactor.core.publisher.Mono;

public interface IReportService {
    public Mono<ReportResponseDTO> generateReport(QueryReportDTO query);
}
