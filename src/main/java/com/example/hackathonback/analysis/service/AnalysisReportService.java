package com.example.hackathonback.analysis.service;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import com.example.hackathonback.analysis.repository.AnalysisReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 분석 리포트 비즈니스 로직을 처리하는 서비스 클래스.
 * - PR 분석 결과 저장
 * - 분석 리포트 조회
 */
@Service
@Transactional(readOnly = true)
public class AnalysisReportService {

    private final AnalysisReportRepository repository;

    public AnalysisReportService(AnalysisReportRepository repository) {
        this.repository = repository;
    }

    /**
     * 새로운 분석 리포트를 생성하고 DB에 저장합니다.
     *
     * @param prUrl  분석 대상 PR(Pull Request)의 URL
     * @param result 코드 분석 결과
     * @return 저장된 AnalysisReport 객체
     */
    @Transactional
    public AnalysisReport saveReport(String prUrl, String result) {
        AnalysisReport report = AnalysisReport.builder()
                .prUrl(prUrl)
                .analysisResult(result)
                .build();
        return repository.save(report);
    }

    /**
     * 모든 분석 리포트를 조회합니다.
     *
     * @return 분석 리포트 리스트
     */
    public List<AnalysisReport> findAllReports() {
        return repository.findAll();
    }

    /**
     * ID로 분석 리포트를 조회합니다.
     *
     * @param id 분석 리포트 ID
     * @return Optional<AnalysisReport>
     */
    public Optional<AnalysisReport> findReportById(Long id) {
        return repository.findById(id);
    }
}
