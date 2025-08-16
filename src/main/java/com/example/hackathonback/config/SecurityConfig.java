package com.example.hackathonback.config;

import com.example.hackathonback.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // 공개 경로 (정적/문서/OAuth)
                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/oauth2/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // 인증 필요 경로
                        .requestMatchers("/api/git/**").authenticated()
                        // 그 외는 우선 허용 (운영 시 정책에 맞게 조정)
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/user", true)
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                )
                // REST API 중심이면 CSRF 비활성화
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
