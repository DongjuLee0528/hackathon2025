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

/**
 * OAuth2 로그인 시 사용자 정보를 처리하는 커스텀 서비스
 * - 이메일로 사용자 식별
 * - 존재하지 않으면 자동 회원가입
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 기본 OAuth2 사용자 정보 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 예: github, gitlab
        String email = (String) oAuth2User.getAttributes().get("email");
        String username = (String) oAuth2User.getAttributes().get("name");

        // 이메일 필수: 없으면 예외 발생
        if (email == null) {
            throw new RuntimeException("GitHub 계정에 이메일이 존재하지 않습니다.");
        }

        // 사용자 정보가 없으면 자동 회원가입
        userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .email(email)
                        .username(username)
                        .provider(registrationId)
                        .build()
                )
        );

        // ROLE_USER 권한과 함께 사용자 인증 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_USER"), // 권한 부여
                oAuth2User.getAttributes(),               // 사용자 정보
                "id"                                      // OAuth 사용자 고유 식별 키 (GitHub은 "id")
        );
    }
}
