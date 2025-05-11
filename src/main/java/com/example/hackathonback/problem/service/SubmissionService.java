package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.repository.CodeSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [한 줄 요약] 코드 제출 이력을 관리하는 서비스 클래스
 *
 * 사용자의 코드 제출 정보를 저장하거나, 사용자별 제출 이력을 조회하는 로직을 담당합니다.
 */
@Service
public class SubmissionService {

    private final CodeSubmissionRepository codeSubmissionRepository; // [한 줄 요약] 코드 제출 정보를 조회/저장하는 JPA 리포지토리

    // [한 줄 요약] 생성자를 통한 의존성 주입
    public SubmissionService(CodeSubmissionRepository codeSubmissionRepository) {
        this.codeSubmissionRepository = codeSubmissionRepository;
    }

    /**
     * [한 줄 요약] 특정 사용자 ID로 제출한 코드 목록 조회
     *
     * 해당 사용자가 지금까지 제출한 모든 코드 이력을 리스트 형태로 반환합니다.
     * 사용자 ID는 문자열(String) 형식으로 전달되며, 내부적으로 리포지토리의 쿼리 메서드를 호출합니다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 코드 제출 이력 리스트
     */
    public List<CodeSubmission> getSubmissionsByUserId(String userId) {
        return codeSubmissionRepository.findByUserId(userId);
    }
}
