package com.example.hackathonback.git.dto;

import lombok.Data;

@Data
public class GitUserInfoDto {
    private String username;
    private String email;
    private String provider;
}
