package com.example.hackathonback.problem.dto;

import com.example.hackathonback.problem.entity.ProblemTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // [한 줄 요약] 모든 필드에 대해 getter/setter 자동 생성 (Lombok)
public class ProblemGenerationRequestDto {

    @NotNull(message = "난이도를 입력해주세요.")
    @Schema(description = "문제 난이도 (예: EASY, MEDIUM, HARD)", example = "EASY")
    private String difficulty; // [한 줄 요약] 문제 난이도 지정 필드

    @NotNull(message = "문제 주제를 선택해주세요.")
    @Schema(description = "문제 태그", implementation = ProblemTag.class)
    private ProblemTag tag; // [한 줄 요약] 문제의 주제 또는 카테고리 (열거형)

    @Schema(description = "사용 언어", example = "JAVA")
    private String language; // [한 줄 요약] 문제 생성에 사용할 프로그래밍 언어 (선택사항)

    @Schema(description = "문제 유형", example = "OUTPUT")
    private String problemType; // [한 줄 요약] 문제 유형 (예: OUTPUT, FUNCTION 등)
}
