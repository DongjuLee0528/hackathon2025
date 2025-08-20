package com.example.hackathonback.config;

import com.example.hackathonback.jwt.JwtAuthenticationEntryPoint;
import com.example.hackathonback.jwt.JwtAuthenticationFilter;
import com.example.hackathonback.jwt.JwtTokenProvider;
import com.example.hackathonback.security.CustomOAuth2UserService;
import com.example.hackathonback.config.OAuth2FailureHandler;
import com.example.hackathonback.config.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                // CORS 설정은 CorsConfig의 Bean을 사용 (중복 Bean 제거)
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight 허용
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**",
                                "/health", "/actuator/**",
                                "/", "/index.html",
                                "/favicon.ico", "/assets/**",
                                "/oauth2/**", "/login/**",
                                "/user" // 로그인 상태 확인용
                        ).permitAll()
                        .requestMatchers(
                                "/judge/**",
                                "/submissions/**",
                                "/api/gpt/review",
                                "/problems/gpt-recommend",
                                "/api/gpt/problem/generate",
                                "/user/score"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/oauth2/authorization/github")
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
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
