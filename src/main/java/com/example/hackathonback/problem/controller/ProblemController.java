package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.GptRecommendationRequestDto;
import com.example.hackathonback.problem.dto.RecommendedProblemDto;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import com.example.hackathonback.problem.dto.ProblemResponseDto;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.service.GptRecommendationService;
import com.example.hackathonback.problem.service.ProblemService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final GptRecommendationService gptRecommendationService;
    private final ProblemService problemService;

    @PostMapping("/problems/gpt-recommend")
    public ResponseEntity<?> recommendByGpt(@RequestBody GptRecommendationRequestDto dto) {
        List<RecommendedProblemDto> problems = gptRecommendationService.getRecommendedProblems(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "추천 문제 목록입니다.",
                "data", problems
        ));
    }

    @PostMapping("/generate")
    public ResponseEntity<ProblemResponseDto> generate(@RequestBody ProblemGenerationRequestDto request) throws Exception {
        Problem problem = problemService.generateProblem(request.getTag().name(), request.getDifficulty(), "java");

        ProblemResponseDto response = new ProblemResponseDto();
        response.setTitle(problem.getTitle());
        response.setDescription(problem.getDescription());
        response.setInputFormat(problem.getInputFormat());
        response.setOutputFormat(problem.getOutputFormat());
        response.setExampleInput(problem.getExampleInput());
        response.setExampleOutput(problem.getExampleOutput());

        return ResponseEntity.ok(response);
    }
}
