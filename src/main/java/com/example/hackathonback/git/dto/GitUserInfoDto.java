package com.example.hackathonback.git.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GitUserInfoDto {
    private String username;
    private String email;
    private String provider;
}
