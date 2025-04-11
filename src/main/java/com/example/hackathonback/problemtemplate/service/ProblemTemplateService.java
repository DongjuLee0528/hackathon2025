package com.example.hackathonback.problemtemplate.service;

import com.example.hackathonback.problemtemplate.dto.ProblemTemplateDto;
import com.example.hackathonback.problemtemplate.entity.ProblemTemplate;
import com.example.hackathonback.problemtemplate.repository.ProblemTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemTemplateService {

    private final ProblemTemplateRepository repository;

    public ProblemTemplate create(ProblemTemplateDto dto) {
        ProblemTemplate template = ProblemTemplate.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .tags(dto.getTags())
                .difficulty(dto.getDifficulty())
                .build();
        return repository.save(template);
    }

    public List<ProblemTemplate> findAll() {
        return repository.findAll();
    }

    public ProblemTemplate findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
