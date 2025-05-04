package com.example.hackathonback.repository.controller;

import com.example.hackathonback.repository.dto.RepositoryDto;
import com.example.hackathonback.repository.service.RepositoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자의 Git 저장소 등록을 처리하는 컨트롤러
 */
@RestController
public class RepositoryController {

    private final RepositoryService repositoryService;

    // 생성자 주입
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * 저장소 등록 요청 API
     * - 로그인된 사용자의 이메일 정보를 기반으로 저장소를 등록
     *
     * @param repositoryDto 등록할 저장소 정보
     */
    @PostMapping("/register/repository")
    public void registerRepository(@RequestBody RepositoryDto repositoryDto) {
        // 현재 로그인한 사용자 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth2 사용자 객체에서 이메일 추출
        String email = oauth2User.getAttribute("email");

        // 저장소 등록 서비스 실행
        repositoryService.registerRepository(email, repositoryDto);
    }
}
