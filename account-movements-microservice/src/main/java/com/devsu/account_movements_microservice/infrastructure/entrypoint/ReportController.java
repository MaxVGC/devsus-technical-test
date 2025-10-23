package com.devsu.account_movements_microservice.infrastructure.entrypoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.account_movements_microservice.application.dtos.in.QueryReportDTO;
import com.devsu.account_movements_microservice.application.dtos.out.ReportResponseDTO;
import com.devsu.account_movements_microservice.application.services.IReportService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;

    @GetMapping
    public ResponseEntity<Mono<ReportResponseDTO>> generateReport(QueryReportDTO query) {
        return ResponseEntity.ok(reportService.generateReport(query));
    }
    
}
