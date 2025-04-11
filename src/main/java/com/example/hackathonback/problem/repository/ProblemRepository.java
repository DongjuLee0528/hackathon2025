package com.example.hackathonback.problem.repository;

import com.example.hackathonback.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
