package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import com.example.hackathonback.problem.dto.ProblemResponseDto;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

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
