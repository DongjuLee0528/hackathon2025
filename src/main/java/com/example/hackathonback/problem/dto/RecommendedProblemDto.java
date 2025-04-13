
package com.example.hackathonback.problem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendedProblemDto {
    private String title;
    private String difficulty;
    private List<String> tags;
}
