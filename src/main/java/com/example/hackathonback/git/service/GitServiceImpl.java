package com.example.hackathonback.git.service;

import com.example.hackathonback.git.client.GitApiClient;
import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.entity.GitRepo;
import com.example.hackathonback.git.repository.GitRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitServiceImpl implements GitService {

    private final GitApiClient gitApiClient;
    private final GitRepoRepository gitRepoRepository;

    @Override
    public List<GitRepoDto> getUserRepositories(String provider, String token) {
        return gitApiClient.fetchRepositories(provider, token);
    }

    @Override
    public void saveRepository(GitRepoDto dto) {
        GitRepo repo = GitRepo.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .owner(dto.getOwner())
                .provider(dto.getProvider())
                .description(dto.getDescription())
                .userId(1L) // TODO: OAuth 연동 후 SecurityContext에서 로그인 유저 ID로 교체
                .build();
        gitRepoRepository.save(repo);
    }
}
