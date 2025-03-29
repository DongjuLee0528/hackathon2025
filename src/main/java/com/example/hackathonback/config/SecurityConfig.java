package com.example.hackathonback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/git/**").authenticated()  // 인증 필요
                        .requestMatchers("/", "/login", "/css/**", "/oauth2/**").permitAll() // 로그인 페이지 등 허용
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/user", true) // 로그인 성공 시 이동할 기본 URL
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
