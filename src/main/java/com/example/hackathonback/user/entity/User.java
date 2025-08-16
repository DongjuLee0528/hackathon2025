package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email", unique = true),
                @Index(name = "idx_users_github_id", columnList = "githubId", unique = true)
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 이메일 (GitHub에서 비공개면 null 가능) */
    @Column(unique = true)
    private String email;

    /** 닉네임/별칭 (기존 필드 유지) */
    private String username;

    /** 로그인 제공자: github, gitlab 등 */
    private String provider;

    /** GitHub 고유 ID (GitHub 연동 사용자만) */
    @Column(unique = true)
    private Long githubId;

    /** GitHub 로그인 아이디(login) */
    private String login;

    /** 실명/프로필 이름(name) */
    private String name;

    /** 프로필 이미지 URL */
    private String avatarUrl;

    /** 사용자 이름 갱신 메서드(기존 유지) */
    public void updateUsername(String username) {
        this.username = username;
    }
}
