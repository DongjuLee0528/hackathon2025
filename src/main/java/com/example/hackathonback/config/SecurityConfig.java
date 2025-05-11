package com.example.hackathonback.config;

import com.example.hackathonback.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor // [한 줄 요약] final 필드를 파라미터로 받는 생성자 자동 생성 (의존성 주입에 사용)
// 이 클래스에서는 customOAuth2UserService를 생성자를 통해 주입받습니다.

@Configuration // [한 줄 요약] Spring Security 설정 클래스임을 명시
// Spring Boot가 이 클래스를 읽고 보안 설정을 적용할 수 있도록 해줍니다.
public class SecurityConfig {

    // [한 줄 요약] 사용자 정의 OAuth2 서비스 클래스 (GitHub 사용자 정보 처리)
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * [한 줄 요약] Spring Security 필터 체인 설정 메서드
     *
     * HttpSecurity 객체를 통해 경로별 접근 권한, OAuth2 로그인, CSRF 설정 등을 구성합니다.
     * 이 메서드에서 반환되는 SecurityFilterChain이 실제 애플리케이션에 적용됩니다.
     *
     * @param http HttpSecurity 객체 (Spring Security 제공)
     * @return SecurityFilterChain
     * @throws Exception 설정 중 예외 발생 가능
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // [한 줄 요약] 경로별 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/git/**").authenticated() // 인증된 사용자만 접근 가능
                        .requestMatchers("/", "/login", "/css/**", "/oauth2/**").permitAll() // 로그인, 정적 자원, OAuth2 경로는 모두 허용
                        .anyRequest().permitAll() // 나머지 모든 요청도 허용 (운영 시 보안 정책에 따라 조정 필요)
                )
                // [한 줄 요약] OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/user", true) // 로그인 성공 시 리디렉션할 기본 URL
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // 사용자 정보 조회 시 사용자 정의 서비스 사용
                )
                // [한 줄 요약] CSRF 보호 비활성화 (REST API 서버에선 종종 비활성화)
                .csrf(csrf -> csrf.disable())
                .build(); // [한 줄 요약] SecurityFilterChain 객체 생성 및 반환
    }
}
