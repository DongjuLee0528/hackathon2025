package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.repository.CodeSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 코드 제출 이력을 관리하는 서비스 클래스
 */
@Service
public class SubmissionService {

    private final CodeSubmissionRepository codeSubmissionRepository;

    // 생성자를 통한 레포지토리 의존성 주입
    public SubmissionService(CodeSubmissionRepository codeSubmissionRepository) {
        this.codeSubmissionRepository = codeSubmissionRepository;
    }

    /**
     * 특정 사용자 ID로 제출한 코드 목록 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 코드 제출 이력 리스트
     */
    public List<CodeSubmission> getSubmissionsByUserId(String userId) {
        return codeSubmissionRepository.findByUserId(userId);
    }
}
