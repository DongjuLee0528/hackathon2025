package com.example.hackathonback.git.dto;

import lombok.Data;

@Data // 모든 필드에 대해 Getter/Setter, toString, equals, hashCode, 생성자 등을 자동 생성
public class GitRepoDto {

    private String name;        // 리포지토리 이름
    private String url;         // 리포지토리 URL
    private String owner;       // 리포지토리 소유자 (예: 사용자명 또는 조직명)
    private String provider;    // Git 제공자 (예: github, gitlab 등)
    private String description; // 리포지토리 설명
}
