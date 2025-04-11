package com.example.hackathonback.problemtemplate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemTemplateDto {
    private String title;
    private String description;
    private String tags;
    private String difficulty;
}
