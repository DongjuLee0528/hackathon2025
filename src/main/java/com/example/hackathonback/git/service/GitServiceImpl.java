package com.example.hackathonback.git.service;

import com.example.hackathonback.git.client.GitApiClient;
import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.entity.GitRepo;
import com.example.hackathonback.git.repository.GitRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 서비스 계층 컴포넌트로 등록
@RequiredArgsConstructor // final 필드에 대해 생성자 자동 생성
public class GitServiceImpl implements GitService {

    private final GitApiClient gitApiClient;           // 외부 API(GitHub/GitLab) 호출 클라이언트
    private final GitRepoRepository gitRepoRepository; // Git 리포지토리 저장소

    /**
     * 사용자의 Git 리포지토리 목록을 외부 API에서 조회
     *
     * @param provider Git 제공자 (github, gitlab)
     * @param token OAuth 액세스 토큰
     * @return 리포지토리 DTO 리스트
     */
    @Override
    public List<GitRepoDto> getUserRepositories(String provider, String token) {
        return gitApiClient.fetchRepositories(provider, token);
    }

    /**
     * 사용자가 선택한 리포지토리를 DB에 저장
     *
     * @param dto 저장할 리포지토리 정보
     * @param userId 현재 로그인한 사용자 ID
     */
    @Override
    public void saveRepository(GitRepoDto dto, Long userId) {
        GitRepo repo = GitRepo.builder()
                .name(dto.getName())              // 리포지토리 이름
                .url(dto.getUrl())                // 리포지토리 URL
                .owner(dto.getOwner())            // 소유자
                .provider(dto.getProvider())      // 제공자(github/gitlab)
                .description(dto.getDescription())// 설명
                .userId(userId)                   // 현재 로그인한 사용자 ID
                .build();

        gitRepoRepository.save(repo); // DB에 저장
    }
}
