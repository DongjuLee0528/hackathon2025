package com.example.hackathonback.analysis.repository;

import com.example.hackathonback.analysis.entity.AnalysisReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AnalysisReport 엔티티를 위한 JPA 리포지토리
 * - 기본적인 CRUD 메서드를 JpaRepository로부터 상속받아 사용 가능
 * - 필요한 경우 사용자 정의 쿼리 메서드 추가 가능
 */
public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, Long> {
    // 예: List<AnalysisReport> findByPrUrl(String prUrl);
}
