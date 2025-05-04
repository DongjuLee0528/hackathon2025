package com.example.hackathonback.config;

import com.example.hackathonback.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor // final 필드를 포함한 생성자 자동 생성 (DI 용도)
@Configuration // 설정 클래스임을 명시
public class SecurityConfig {

    // 사용자 정의 OAuth2 사용자 서비스 주입
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * Spring Security 필터 체인을 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/git/**").authenticated() // /api/git/** 경로는 인증 필요
                        .requestMatchers("/", "/login", "/css/**", "/oauth2/**").permitAll() // 이 경로들은 누구나 접근 가능
                        .anyRequest().permitAll() // 나머지 모든 요청도 접근 허용
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/user", true) // 로그인 성공 시 리디렉션할 기본 URL
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // 사용자 정보 처리 서비스 설정
                )
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 서버 등에서는 종종 사용 안 함)
                .build(); // SecurityFilterChain 빌드
    }
}
