package com.example.hackathonback.gpt.controller;

import com.example.hackathonback.gpt.service.GptProblemService;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * GPT를 활용하여 문제를 생성하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/api/gpt") // 이 컨트롤러의 기본 URL 경로 설정
public class GptProblemController {

    private final GptProblemService gptProblemService;

    /**
     * GptProblemController 생성자
     * @param gptProblemService 문제 생성 로직을 담당하는 서비스 클래스
     */
    public GptProblemController(GptProblemService gptProblemService) {
        this.gptProblemService = gptProblemService;
    }

    /**
     * POST 요청을 통해 문제를 생성하는 API 엔드포인트
     * @param request 사용자가 요청한 문제의 언어와 문제 유형 정보를 담은 DTO
     * @return 생성된 문제를 문자열로 반환
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateProblem(@RequestBody ProblemGenerationRequestDto request) {
        // 문제 생성 서비스 호출
        String result = gptProblemService.generateProblem(request.getLanguage(), request.getProblemType());
        // 생성된 결과를 응답으로 반환
        return ResponseEntity.ok(result);
    }
}
