package com.example.hackathonback.git.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class GitRepoDto {
    private String name;
    private String url;
    private String owner;
    private String provider;
    private String description;
}
