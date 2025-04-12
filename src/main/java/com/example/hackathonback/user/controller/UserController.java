package com.example.hackathonback.user.controller;

import com.example.hackathonback.user.service.UserScoreService;
import com.example.hackathonback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserScoreService userScoreService;

    @GetMapping("/user")
    public String userInfo(@AuthenticationPrincipal OAuth2User principal) {
        return "GitLab 사용자 이름: " + principal.getAttribute("name") + "<br>" +
                "이메일: " + principal.getAttribute("email") + "<br>" +
                "아이디: " + principal.getAttribute("username");
    }

    @GetMapping("/user/score")
    public String getUserRank(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        Long userId = userService.findUserIdByEmail(email);
        String rank = userScoreService.getUserRank(userId);
        double avg = userScoreService.getUserAverageScore(userId);
        return "등급: " + rank + " / 평균 점수: " + avg;
    }
}
