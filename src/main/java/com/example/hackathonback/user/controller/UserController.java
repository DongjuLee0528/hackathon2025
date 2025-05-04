package com.example.hackathonback.user.controller;

import com.example.hackathonback.user.service.UserScoreService;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 관련 정보를 제공하는 컨트롤러
 * - 사용자 기본 정보
 * - 사용자 점수 및 등급
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;             // 사용자 ID 조회용
    private final UserScoreService userScoreService;   // 점수 및 등급 관리 서비스

    /**
     * 로그인한 사용자의 OAuth2 정보 반환
     */
    @GetMapping("/user")
    public String userInfo(@AuthenticationPrincipal OAuth2User principal) {
        return "GitLab 사용자 이름: " + principal.getAttribute("name") + "<br>" +
                "이메일: " + principal.getAttribute("email") + "<br>" +
                "아이디: " + principal.getAttribute("username");
    }

    /**
     * 로그인한 사용자의 평균 점수 및 등급 반환
     */
    @GetMapping("/user/score")
    public String getUserRank(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");

        // 이메일로 사용자 ID 조회
        Long userId = userService.findUserIdByEmail(email);

        // 등급 및 평균 점수 조회
        String rank = userScoreService.getUserRank(userId);
        double avg = userScoreService.getUserAverageScore(userId);

        return "등급: " + rank + " / 평균 점수: " + avg;
    }
}
