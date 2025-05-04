package com.example.hackathonback.analysis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // 이 클래스는 JPA 엔티티로, 데이터베이스 테이블과 매핑됨
public class AnalysisReport {

    @Id // 기본 키(primary key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 전략 사용 (MySQL 등의 DB에 적합)
    private Long id;

    private String prUrl; // 분석 대상이 된 PR(Pull Request)의 URL

    @Column(columnDefinition = "TEXT") // 분석 결과를 저장하는 필드, 긴 텍스트를 위해 TEXT 타입 사용
    private String analysisResult;

    private LocalDateTime createdAt = LocalDateTime.now(); // 레코드 생성 시각, 기본값은 현재 시각

    // --- Getter 및 Setter 메서드 ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrUrl() {
        return prUrl;
    }

    public void setPrUrl(String prUrl) {
        this.prUrl = prUrl;
    }

    public String getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(String analysisResult) {
        this.analysisResult = analysisResult;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
