package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "users") // "user"는 예약어일 수 있으므로 테이블 이름을 "users"로 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 기본 키
    private Long id;

    private String email;     // 사용자 이메일 (OAuth 기반)
    private String username;  // 사용자 이름 또는 닉네임
    private String provider;  // 로그인 제공자 (예: github, gitlab)
}
