package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.GptRecommendationRequestDto;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import com.example.hackathonback.problem.dto.RecommendedProblemDto;
import com.example.hackathonback.problem.dto.ProblemResponseDto;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.service.GptRecommendationService;
import com.example.hackathonback.problem.service.ProblemService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // REST API 컨트롤러로 등록
@RequestMapping("/api/problem") // 문제 관련 API 기본 경로
@RequiredArgsConstructor // 생성자 자동 주입 (gptRecommendationService, problemService)
public class ProblemController {

    private final GptRecommendationService gptRecommendationService; // GPT 기반 추천 서비스
    private final ProblemService problemService; // 문제 생성 및 조회 서비스

    /**
     * GPT를 기반으로 추천 문제 목록을 요청
     * @param dto 사용자 요청 DTO (태그, 난이도, 수 등 포함)
     * @return 추천된 문제 리스트
     */
    @PostMapping("/problems/gpt-recommend")
    public ResponseEntity<?> recommendByGpt(@RequestBody GptRecommendationRequestDto dto) {
        List<RecommendedProblemDto> problems = gptRecommendationService.getRecommendedProblems(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "추천 문제 목록입니다.",
                "data", problems
        ));
    }

    /**
     * GPT를 통해 문제를 자동 생성
     * @param request 문제 생성 요청 DTO (태그, 난이도 포함)
     * @return 생성된 문제 정보를 담은 응답 DTO
     */
    @PostMapping("/generate")
    public ResponseEntity<ProblemResponseDto> generate(@RequestBody ProblemGenerationRequestDto request) throws Exception {
        // 문제 생성 (언어는 java로 고정)
        Problem problem = problemService.generateProblem(request.getTag().name(), request.getDifficulty(), "java");

        // 응답 DTO 구성
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
