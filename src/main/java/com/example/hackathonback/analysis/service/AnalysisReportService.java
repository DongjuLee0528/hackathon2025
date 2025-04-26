package com.example.hackathonback.analysis.service;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import com.example.hackathonback.analysis.repository.AnalysisReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalysisReportService {

    private final AnalysisReportRepository repository;

    public AnalysisReportService(AnalysisReportRepository repository) {
        this.repository = repository;
    }

    public AnalysisReport saveReport(String prUrl, String result) {
        AnalysisReport report = new AnalysisReport();
        report.setPrUrl(prUrl);
        report.setAnalysisResult(result);
        return repository.save(report);
    }

    public List<AnalysisReport> findAllReports() {
        return repository.findAll();
    }

    public Optional<AnalysisReport> findReportById(Long id) {
        return repository.findById(id);
    }
}