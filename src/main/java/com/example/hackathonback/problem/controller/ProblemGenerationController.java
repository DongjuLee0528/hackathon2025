package com.example.hackathonback.problem.controller;

import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.service.ProblemGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problem/generate")
@RequiredArgsConstructor
public class ProblemGenerationController {

    private final ProblemGeneratorService problemGeneratorService;

    @PostMapping
    public Problem generate(@RequestBody ProblemGenerationRequestDto dto) {
        return problemGeneratorService.generateProblem(dto.getDifficulty(), dto.getTag());
    }
}
