package com.example.hackathonback.gpt.controller;

import com.example.hackathonback.gpt.service.GptProblemService;
import com.example.hackathonback.problem.dto.ProblemGenerationRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gpt")
public class GptProblemController {

    private final GptProblemService gptProblemService;

    public GptProblemController(GptProblemService gptProblemService) {
        this.gptProblemService = gptProblemService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateProblem(@RequestBody ProblemGenerationRequestDto request) {
        String result = gptProblemService.generateProblem(request.getLanguage(), request.getProblemType());
        return ResponseEntity.ok(result);
    }
}