package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.GptRecommendationRequestDto;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import com.example.hackathonback.problem.dto.RecommendedProblemDto;
import com.example.hackathonback.problem.dto.ProblemResponseDto;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.service.GptRecommendationService;
import com.example.hackathonback.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController // REST API 컨트롤러로 등록
@RequestMapping("/api/problem") // 문제 관련 API 기본 경로
@RequiredArgsConstructor // 생성자 자동 주입 (gptRecommendationService, problemService)
public class ProblemController {

    private final GptRecommendationService gptRecommendationService; // GPT 기반 추천 서비스
    private final ProblemService problemService;                     // 문제 생성 및 조회 서비스

    /**
     * GPT를 기반으로 추천 문제 목록을 요청
     */
    @PostMapping(
            value = "/problems/gpt-recommend",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> recommendByGpt(@RequestBody GptRecommendationRequestDto dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().body(error("요청 본문이 비어 있습니다."));
        }
        try {
            List<RecommendedProblemDto> problems = gptRecommendationService.getRecommendedProblems(dto);
            return ResponseEntity.ok(success("추천 문제 목록입니다.", Map.of("items", problems)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(nonNullMsg(e, "잘못된 요청입니다.")));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("추천 문제 처리 중 오류가 발생했습니다."));
        }
    }

    /**
     * GPT를 통해 문제를 자동 생성
     * - 언어는 java 고정
     */
    @PostMapping(
            value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> generate(@RequestBody ProblemGenerationRequestDto request) {
        if (request == null || request.getTag() == null || !StringUtils.hasText(request.getDifficulty())) {
            return ResponseEntity.badRequest().body(error("tag와 difficulty는 필수입니다."));
        }

        try {
            Problem problem = problemService.generateProblem(
                    request.getTag().name(),
                    request.getDifficulty().trim(),
                    "java"
            );

            ProblemResponseDto response = new ProblemResponseDto();
            response.setTitle(problem.getTitle());
            response.setDescription(problem.getDescription());
            response.setInputFormat(problem.getInputFormat());
            response.setOutputFormat(problem.getOutputFormat());
            response.setExampleInput(problem.getExampleInput());
            response.setExampleOutput(problem.getExampleOutput());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(nonNullMsg(e, "잘못된 요청입니다.")));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(error("문제 생성 처리 중 오류가 발생했습니다."));
        }
    }

    /* ---------- 공통 응답 유틸 ---------- */

    private Map<String, Object> success(String message, Map<String, Object> data) {
        return Map.of(
                "status", "success",
                "message", message,
                "data", data == null ? Collections.emptyMap() : data
        );
    }

    private Map<String, Object> error(String message) {
        return Map.of(
                "status", "error",
                "message", message
        );
    }

    private String nonNullMsg(Exception e, String fallback) {
        return (e.getMessage() == null || e.getMessage().isBlank()) ? fallback : e.getMessage();
    }
}
