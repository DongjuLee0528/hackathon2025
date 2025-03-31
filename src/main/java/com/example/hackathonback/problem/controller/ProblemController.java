package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.JudgeRequestDto;
import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.example.hackathonback.problem.service.ProblemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/problem")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    // 사용자 코드 제출 시 GPT를 통해 채점 요청
    @PostMapping("/judge")
    public ResponseEntity<JudgeResponseDto> judgeCode(@RequestBody JudgeRequestDto dto) throws JsonProcessingException {
        JudgeResponseDto result = problemService.judgeCode(dto);
        return ResponseEntity.ok(result);
    }
}
