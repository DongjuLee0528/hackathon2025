package com.example.hackathonback.user.controller;

import com.example.hackathonback.user.service.UserScoreService;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 관련 정보를 제공하는 컨트롤러
 * - 사용자 기본 정보
 * - 사용자 점수 및 등급
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;             // 사용자 ID 조회용
    private final UserScoreService userScoreService;   // 점수 및 등급 관리 서비스

    /**
     * 로그인한 사용자의 기본 정보 반환 (JWT 기반 인증)
     * GET /api/user
     */
    @GetMapping
    public String userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "인증되지 않은 사용자입니다.";
        }
        String email = userDetails.getUsername(); // JwtTokenProvider 에서 username=email 로 세팅
        return "로그인 사용자 이메일: " + email;
    }

    /**
     * 로그인한 사용자의 평균 점수 및 등급 반환
     * GET /api/user/score
     */
    @GetMapping("/score")
    public String getUserRank(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "인증되지 않은 사용자입니다.";
        }
        String email = userDetails.getUsername();

        // 이메일로 사용자 ID 조회
        Long userId = userService.findUserIdByEmail(email);

        // 등급 및 평균 점수 조회
        String rank = userScoreService.getUserRank(userId);
        double avg = userScoreService.getUserAverageScore(userId);

        return "등급: " + rank + " / 평균 점수: " + avg;
    }
}
