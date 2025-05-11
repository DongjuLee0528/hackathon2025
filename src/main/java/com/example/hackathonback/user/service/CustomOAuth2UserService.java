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

@Slf4j // [한 줄 요약] 로깅을 위한 Lombok 어노테이션 (log.info 등 사용 가능)
@Service // [한 줄 요약] 스프링 컴포넌트로 등록되는 OAuth2 사용자 서비스
@RequiredArgsConstructor // [한 줄 요약] 생성자 주입 자동 생성 (userRepository)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // [한 줄 요약] 사용자 정보 DB 저장/조회용 JPA 리포지토리

    /**
     * [한 줄 요약] OAuth2 로그인 후 사용자 정보를 불러오고 DB에 저장 또는 업데이트
     *
     * 1. OAuth2UserRequest로부터 provider, 사용자 식별 정보 추출
     * 2. 사용자 email로 기존 사용자 조회 후 업데이트 또는 새로 저장
     * 3. DefaultOAuth2User로 권한과 사용자 정보 반환
     *
     * @param userRequest OAuth2 인증 요청 객체
     * @return OAuth2User 사용자 정보 객체
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest); // 기본 구현을 통해 사용자 정보 로드

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // ex: github
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // ex: "id"

        String email = (String) oAuth2User.getAttributes().get("email");
        String username = (String) oAuth2User.getAttributes().get("name");

        // [한 줄 요약] 이메일 누락 시 예외 처리
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("OAuth 계정에 이메일이 존재하지 않습니다.");
        }

        // [한 줄 요약] 이메일 기준으로 사용자 존재 여부 확인 후 저장 또는 업데이트
        userRepository.findByEmail(email)
                .map(user -> {
                    user.updateUsername(username); // 이름 업데이트
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .username(username)
                                .provider(registrationId)
                                .build()
                ));

        log.info("OAuth 로그인 성공 - Provider: {}, Email: {}, Name: {}", registrationId, email, username);

        // [한 줄 요약] 인증된 사용자 정보를 ROLE_USER와 함께 반환
        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_USER"), // 권한 부여
                oAuth2User.getAttributes(),               // 사용자 속성 전달
                userNameAttributeName                     // 기본 식별자 설정
        );
    }
}
