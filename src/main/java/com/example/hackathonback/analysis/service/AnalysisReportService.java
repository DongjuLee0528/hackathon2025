package com.example.hackathonback.analysis.service;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import com.example.hackathonback.analysis.repository.AnalysisReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // 이 클래스는 비즈니스 로직을 담당하는 서비스 계층임을 나타냄
public class AnalysisReportService {

    private final AnalysisReportRepository repository;

    // 생성자를 통해 Repository 주입
    public AnalysisReportService(AnalysisReportRepository repository) {
        this.repository = repository;
    }

    /**
     * 분석 리포트를 생성 및 저장
     * @param prUrl PR(Pull Request)의 URL
     * @param result 분석 결과 텍스트
     * @return 저장된 AnalysisReport 객체
     */
    public AnalysisReport saveReport(String prUrl, String result) {
        AnalysisReport report = new AnalysisReport();
        report.setPrUrl(prUrl);
        report.setAnalysisResult(result);
        return repository.save(report); // JPA를 통해 DB에 저장
    }

    /**
     * 저장된 모든 분석 리포트를 조회
     * @return AnalysisReport 리스트
     */
    public List<AnalysisReport> findAllReports() {
        return repository.findAll();
    }

    /**
     * 특정 ID의 분석 리포트를 조회
     * @param id 분석 리포트의 ID
     * @return 존재하면 AnalysisReport, 없으면 Optional.empty()
     */
    public Optional<AnalysisReport> findReportById(Long id) {
        return repository.findById(id);
    }
}
