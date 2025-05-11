package com.example.hackathonback.analysis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // [한 줄 요약] GitHub PR 분석 결과를 저장하는 JPA 엔티티 클래스
// 이 클래스는 특정 PR에 대한 코드 분석 결과를 데이터베이스에 저장하는 역할을 합니다.
public class AnalysisReport {

    @Id // [한 줄 요약] 기본 키 필드
    @GeneratedValue(strategy = GenerationType.IDENTITY) // [한 줄 요약] DB의 AUTO_INCREMENT 전략 사용
    // ID는 DB에서 자동 생성되며, MySQL과 같은 RDBMS에서 사용하기 적합한 설정입니다.
    private Long id;

    private String prUrl; // [한 줄 요약] 분석 대상인 PR(Pull Request)의 URL
    // 분석된 GitHub Pull Request의 링크를 저장합니다. 해당 URL로 분석 대상을 추적할 수 있습니다.

    @Column(columnDefinition = "TEXT") // [한 줄 요약] 분석 결과를 저장하는 필드 (텍스트 타입)
    // 코드 분석 결과는 길이가 길 수 있으므로 TEXT 타입으로 설정합니다.
    private String analysisResult;

    private LocalDateTime createdAt = LocalDateTime.now(); // [한 줄 요약] 분석 결과 생성 시각
    // 객체가 생성되는 시점의 시간으로 자동 설정됩니다. 레코드 생성 일자 추적에 사용됩니다.

    // --- Getter 및 Setter 메서드 ---
    // 각 필드에 접근하거나 수정하기 위한 getter/setter 메서드입니다.

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
