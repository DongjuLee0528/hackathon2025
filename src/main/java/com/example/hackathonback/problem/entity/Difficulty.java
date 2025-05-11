package com.example.hackathonback.problem.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "문제 난이도") // [한 줄 요약] 문제 난이도를 나타내는 열거형 (Swagger 문서용 설명 포함)
// 이 열거형은 문제 생성 시 선택할 수 있는 난이도 수준(EASY, MEDIUM, HARD)을 정의합니다.
public enum Difficulty {

    @Schema(description = "쉬움") // [한 줄 요약] 가장 쉬운 난이도
    EASY,

    @Schema(description = "중간") // [한 줄 요약] 보통 난이도
    MEDIUM,

    @Schema(description = "어려움") // [한 줄 요약] 높은 난이도
    HARD
}
