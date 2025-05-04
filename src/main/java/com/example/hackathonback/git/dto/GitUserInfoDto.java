package com.example.hackathonback.git.dto;

import lombok.Data;

@Data // 모든 필드에 대해 Getter/Setter, toString, equals, hashCode, 생성자 등을 자동 생성
public class GitUserInfoDto {

    private String username;  // Git 사용자 이름 (로그인 ID 또는 닉네임)
    private String email;     // Git 사용자 이메일
    private String provider;  // OAuth 제공자 (예: github, gitlab 등)
}
