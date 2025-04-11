package com.example.hackathonback.problemtemplate.repository;

import com.example.hackathonback.problemtemplate.entity.ProblemTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemTemplateRepository extends JpaRepository<ProblemTemplate, Long> {
}
