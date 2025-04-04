package com.example.hackathonback.git.service;

import com.example.hackathonback.git.dto.GitRepoDto;
import java.util.List;

public interface GitService {
    List<GitRepoDto> getUserRepositories(String provider, String token);

    // ✅ userId를 매개변수로 받도록 수정
    void saveRepository(GitRepoDto repoDto, Long userId);
}
