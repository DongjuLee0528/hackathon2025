package com.example.hackathonback.config;

import com.example.hackathonback.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/health", "/actuator/**",
                                "/", "/index.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                        .defaultSuccessUrl("https://thecoder.djloghub.com/oauth/success") // ✅ JWT 안 쓰고 바로 프론트로 이동
                        .failureHandler(oAuth2FailureHandler) // 실패 시 실패 리다이렉트
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 필요 시 프론트 엔드포인트로 수정 가능
                );

        return http.build();
    }
}
