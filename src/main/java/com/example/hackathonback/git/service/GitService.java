package com.example.hackathonback.git.service;

import com.example.hackathonback.git.dto.GitRepoDto;
import java.util.List;

/**
 * Git 리포지토리 관련 비즈니스 로직 인터페이스
 */
public interface GitService {

    /**
     * 사용자의 Git 리포지토리 목록을 조회
     *
     * @param provider Git 제공자 (예: github, gitlab)
     * @param token OAuth 액세스 토큰
     * @return GitRepoDto 리스트
     */
    List<GitRepoDto> getUserRepositories(String provider, String token);

    /**
     * 사용자가 선택한 리포지토리를 저장
     *
     * @param repoDto 리포지토리 정보
     * @param userId 현재 로그인한 사용자 ID
     */
    void saveRepository(GitRepoDto repoDto, Long userId); // ✅ userId를 매개변수로 받도록 수정됨
}
