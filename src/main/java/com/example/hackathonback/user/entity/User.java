package com.example.hackathonback.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // ✅ 테이블 이름을 "users"로 변경
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String provider;
}
