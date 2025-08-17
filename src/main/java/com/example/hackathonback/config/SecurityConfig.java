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
                                "/", "/index.html",
                                // 프런트 정적 경로 허용 필요 시 추가
                                "/favicon.ico", "/assets/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/oauth/callback", true)
                        .failureHandler(oAuth2FailureHandler)
                )
                .logout(logout -> logout
                        // 필요시 프런트 루트로
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
