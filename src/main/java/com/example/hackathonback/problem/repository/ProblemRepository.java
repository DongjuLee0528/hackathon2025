package com.example.hackathonback.problem.repository;

import com.example.hackathonback.problem.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Problem 엔티티를 위한 JPA 리포지토리
 * - 문제 데이터의 저장, 조회, 삭제 등을 처리
 */
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    // 필요 시 사용자 정의 쿼리 메서드 추가 가능
    // 예: List<Problem> findByDifficulty(String difficulty);
}
