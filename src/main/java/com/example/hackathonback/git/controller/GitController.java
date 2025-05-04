package com.example.hackathonback.git.controller;

import com.example.hackathonback.git.dto.GitRepoDto;
import com.example.hackathonback.git.service.GitService;
import com.example.hackathonback.user.service.UserService; // ✅ 사용자 서비스 추가
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API 컨트롤러로 등록
@RequestMapping("/api/git") // API 기본 경로
@RequiredArgsConstructor // 생성자 자동 생성 (gitService, userService 주입)
@CrossOrigin(origins = "*") // CORS 허용 (모든 도메인에서 접근 가능)
public class GitController {

    private final GitService gitService;
    private final UserService userService; // ✅ 사용자 서비스 주입

    /**
     * 사용자의 Git 리포지토리 목록을 조회
     * @param provider "github" 또는 "gitlab"
     * @param token OAuth 액세스 토큰
     * @return GitRepoDto 리스트
     */
    @GetMapping("/repos")
    public List<GitRepoDto> getUserRepos(@RequestParam String provider, @RequestParam String token) {
        return gitService.getUserRepositories(provider, token);
    }

    /**
     * 사용자가 선택한 리포지토리를 저장
     * - 현재 로그인한 사용자의 이메일 정보를 기반으로 저장
     * @param repoDto 선택한 리포지토리 정보
     */
    @PostMapping("/select")
    public void selectRepository(@RequestBody GitRepoDto repoDto) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth2 사용자 객체에서 이메일 정보 추출
        String email = oauth2User.getAttribute("email");

        // ✅ 이메일로 DB에 등록된 사용자 ID 조회
        Long userId = userService.findUserIdByEmail(email);

        // 리포지토리 저장 시 사용자 ID와 함께 저장
        gitService.saveRepository(repoDto, userId);
    }
}
