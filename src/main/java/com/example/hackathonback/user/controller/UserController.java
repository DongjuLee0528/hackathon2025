package com.example.hackathonback.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public String userInfo(@AuthenticationPrincipal OAuth2User principal) {
        return "GitLab 사용자 이름: " + principal.getAttribute("name") + "<br>" +
                "이메일: " + principal.getAttribute("email") + "<br>" +
                "아이디: " + principal.getAttribute("username");
    }
}
