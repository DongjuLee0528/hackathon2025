package com.example.hackathonback.git.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자가 선택한 Git 리포지토리를 저장하는 JPA 엔티티 클래스
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitRepo {

    @Id // 기본 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동 증가
    private Long id;

    private String name;        // 리포지토리 이름
    private String url;         // 리포지토리 URL
    private String owner;       // 리포지토리 소유자 (사용자명 또는 조직명)
    private String provider;    // Git 제공자 (예: github, gitlab 등)
    private String description; // 리포지토리 설명

    private Long userId; // 로그인 사용자 ID (OAuth 연동 시 해당 사용자와 연결)
}
