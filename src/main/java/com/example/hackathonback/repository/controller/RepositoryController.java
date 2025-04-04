package com.example.hackathonback.repository.controller;

import com.example.hackathonback.repository.dto.RepositoryDto;
import com.example.hackathonback.repository.service.RepositoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {

    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // ✅ 저장소 등록 API
    @PostMapping("/register/repository")
    public void registerRepository(@RequestBody RepositoryDto repositoryDto) {
        // OAuth2 인증을 통해 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // 사용자의 이메일 가져오기
        String email = oauth2User.getAttribute("email");

        // 저장소 등록 서비스 호출
        repositoryService.registerRepository(email, repositoryDto);
    }
}
