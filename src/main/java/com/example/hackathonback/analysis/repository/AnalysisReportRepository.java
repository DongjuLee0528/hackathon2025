package com.example.hackathonback.analysis.repository;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, Long> {
}