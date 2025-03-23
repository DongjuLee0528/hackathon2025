package com.example.hackathonback.git.controller;

import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.service.GitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/git")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 프론트에서 호출할 경우 CORS 허용
public class GitController {

    private final GitService gitService;

    @GetMapping("/repos")
    public List<GitRepoDto> getUserRepos(@RequestParam String provider, @RequestParam String token) {
        return gitService.getUserRepositories(provider, token);
    }

    @PostMapping("/select")
    public void selectRepository(@RequestBody GitRepoDto repoDto) {
        gitService.saveRepository(repoDto);
    }
}
