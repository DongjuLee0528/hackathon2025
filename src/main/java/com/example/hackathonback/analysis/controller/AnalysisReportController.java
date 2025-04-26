package com.example.hackathonback.analysis.controller;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import com.example.hackathonback.analysis.service.AnalysisReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class AnalysisReportController {

    private final AnalysisReportService service;

    public AnalysisReportController(AnalysisReportService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnalysisReport> getAllReports() {
        return service.findAllReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisReport> getReportById(@PathVariable Long id) {
        return service.findReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}