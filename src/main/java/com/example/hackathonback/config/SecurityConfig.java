package com.example.hackathonback.config;

import com.example.hackathonback.jwt.JwtAuthenticationEntryPoint;
import com.example.hackathonback.jwt.JwtAuthenticationFilter;
import com.example.hackathonback.jwt.JwtTokenProvider;
import com.example.hackathonback.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("${app.frontend.base}")
    private String frontendBase; // 예: https://thecoder.djloghub.com

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // CORS 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //  세션 대신 JWT
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)) //  인증 실패 처리
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/health", "/actuator/**",
                                "/", "/index.html",
                                "/favicon.ico", "/assets/**",
                                "/oauth2/**", "/login/**",
                                "/user" // 로그인 상태 확인용
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/oauth2/authorization/github")
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)  //  JWT 발급 + 쿠키 저장
                        .failureHandler(oAuth2FailureHandler)
                )
                .logout(logout -> logout
                        .deleteCookies("JSESSIONID", "ACCESS_TOKEN", "REFRESH_TOKEN")
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler((req, res, auth) -> {
                            String target = frontendBase.endsWith("/") ? frontendBase : frontendBase + "/";
                            res.sendRedirect(target);
                        })
                );

        //  JWT 필터 추가 (UsernamePasswordAuthenticationFilter 앞에 실행됨)
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
