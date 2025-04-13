package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.User;
import com.example.hackathonback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 예: "github"
        String email = (String) oAuth2User.getAttributes().get("email");
        String username = (String) oAuth2User.getAttributes().get("name");

        // 이메일이 존재하지 않으면 예외 처리
        if (email == null) {
            throw new RuntimeException("GitHub 계정에 이메일이 존재하지 않습니다.");
        }

        // 기존 유저가 없으면 자동 회원가입 처리
        userRepository.findByEmail(email).orElseGet(() ->
            userRepository.save(User.builder()
                    .email(email)
                    .username(username)
                    .provider(registrationId)
                    .build()
            )
        );

        return new DefaultOAuth2User(
            Collections.singleton(() -> "ROLE_USER"),
            oAuth2User.getAttributes(),
            "id" // GitHub OAuth response 내 사용자 식별 키
        );
    }
}