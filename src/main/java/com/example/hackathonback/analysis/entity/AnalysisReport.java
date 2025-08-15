package com.example.hackathonback.analysis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE)    // 빌더 전용 생성자
@Builder                                              // 엔티티 생성 시 가독성 향상
@Entity
@Table(name = "analysis_report")
public class AnalysisReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String prUrl;

    @Lob
    @Column(nullable = false)
    private String analysisResult;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
