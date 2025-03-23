package com.example.hackathonback.git.controller;

import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.service.GitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자 식별 정보 추출 (GitHub 또는 GitLab 기준)
        String email = oauth2User.getAttribute("email");
        String username = oauth2User.getAttribute("login"); // GitHub 기준
        if (username == null) {
            username = oauth2User.getAttribute("username"); // GitLab 기준
        }

        System.out.println("Logged-in user: " + username + " (" + email + ")");

        // 필요한 경우 사용자 식별 정보를 서비스로 넘겨 저장 시 사용할 수 있음
        gitService.saveRepository(repoDto);
    }
}
