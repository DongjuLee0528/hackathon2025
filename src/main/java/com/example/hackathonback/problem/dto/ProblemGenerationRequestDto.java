
package com.example.hackathonback.problem.dto;

import com.example.hackathonback.problem.entity.ProblemTag;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemGenerationRequestDto {

    @NotNull(message = "난이도를 입력해주세요.")
    private String difficulty;

    @NotNull(message = "문제 주제를 선택해주세요.")
    private ProblemTag tag;
}
