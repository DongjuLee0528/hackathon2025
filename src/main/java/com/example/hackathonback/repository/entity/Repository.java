package com.example.hackathonback.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 저장소 정보를 저장하는 JPA 엔티티
 */
@Entity
@Getter
@Setter
public class Repository {

    @Id
    @GeneratedValue // 기본 키 자동 생성 (IDENTITY 전략 생략 시 DB 기본값 사용)
    private Long id;

    private String name;    // 저장소 이름
    private Long userId;    // 사용자 ID (OAuth 연동된 사용자 기준)
}
