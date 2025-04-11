package com.example.hackathonback.problemtemplate.controller;

import com.example.hackathonback.problemtemplate.dto.ProblemTemplateDto;
import com.example.hackathonback.problemtemplate.entity.ProblemTemplate;
import com.example.hackathonback.problemtemplate.service.ProblemTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem-template")
@RequiredArgsConstructor
public class ProblemTemplateController {

    private final ProblemTemplateService service;

    @PostMapping
    public ProblemTemplate create(@RequestBody ProblemTemplateDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<ProblemTemplate> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProblemTemplate getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
