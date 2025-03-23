package com.example.hackathonback.git.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GitRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private String owner;
    private String provider;
    private String description;

    private Long userId; // 로그인 사용자 ID (임시로 설정, OAuth 연동 시 대체 가능)
}
