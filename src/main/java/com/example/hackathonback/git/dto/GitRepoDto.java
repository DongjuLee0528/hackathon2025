package com.example.hackathonback.git.dto;

import lombok.Data;

@Data
public class GitRepoDto {
    private String name;
    private String url;
    private String owner;
    private String provider;
    private String description;
}
