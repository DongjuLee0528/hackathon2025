package com.example.hackathonback.security;

import com.example.hackathonback.user.entity.User;
import com.example.hackathonback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /** ✅ 이메일 기반 사용자 로드 */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        // ROLE_USER 권한 부여
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "", // OAuth 로그인이라 비밀번호는 사용하지 않음
                Collections.singletonList(() -> "ROLE_USER")
        );
    }
}
