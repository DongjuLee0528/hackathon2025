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

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final GptRecommendationService gptRecommendationService;
    private final ProblemService problemService;

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

    private Map<String, Object> success(String message, Map<String, Object> data) {
        return Map.of(
                "status", "success",
                "message", message,
                "data", data == null ? Collections.emptyMap() : data
        );
    }

    private Map<String, Object> error(String message) {
        return Map.of("status", "error", "message", message);
    }

    private String nonNullMsg(Exception e, String fallback) {
        return (e.getMessage() == null || e.getMessage().isBlank()) ? fallback : e.getMessage();
    }
}
