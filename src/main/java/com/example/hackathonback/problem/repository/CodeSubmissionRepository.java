package com.example.hackathonback.problem.repository;

import com.example.hackathonback.problem.entity.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {
    // 필요 시 사용자별 제출 이력 조회 기능 추가 가능
}

