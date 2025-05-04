package com.example.hackathonback.problem.repository;

import com.example.hackathonback.problem.entity.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CodeSubmission 엔티티를 위한 JPA 레포지토리
 * - 사용자 제출 코드 데이터를 DB에서 관리
 */
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {

    /**
     * 특정 사용자 ID로 제출 이력을 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 CodeSubmission 리스트
     */
    List<CodeSubmission> findByUserId(String userId);
}
