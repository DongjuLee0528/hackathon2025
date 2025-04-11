package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.repository.ProblemRepository;
import com.example.hackathonback.problemtemplate.entity.ProblemTemplate;
import com.example.hackathonback.problemtemplate.repository.ProblemTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProblemGeneratorService {

    private final ProblemTemplateRepository templateRepo;
    private final ProblemRepository problemRepo;

    public Problem generateProblem(String difficulty, String tag) {
        // 조건에 맞는 템플릿 검색
        List<ProblemTemplate> candidates = templateRepo.findAll().stream()
                .filter(t -> t.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(t -> t.getTags().toLowerCase().contains(tag.toLowerCase()))
                .toList();

        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("해당 조건에 맞는 템플릿이 없습니다.");
        }

        // 무작위 템플릿 선택
        ProblemTemplate selected = candidates.get(new Random().nextInt(candidates.size()));

        // 문제 조합 생성
        Problem problem = Problem.builder()
                .title(selected.getTitle() + " (랜덤 생성)")
                .description(selected.getDescription())
                .inputExample("입력 예시 없음")
                .outputExample("출력 예시 없음")
                .difficulty(selected.getDifficulty())
                .tags(selected.getTags())
                .templateId(selected.getId())
                .build();

        return problemRepo.save(problem);
    }
}
