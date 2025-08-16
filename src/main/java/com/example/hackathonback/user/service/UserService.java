package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.User;
import com.example.hackathonback.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /** 이메일로 사용자 ID 조회 (조회 전용 트랜잭션) */
    @Transactional(Transactional.TxType.SUPPORTS)
    public Long findUserIdByEmail(String rawEmail) {
        String email = normalizeEmail(rawEmail);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + email));
        return user.getId();
    }

    /** [선택] GitHub OAuth 정보로 사용자 찾거나 생성 */
    @Transactional
    public User findOrCreateByGithub(Long githubId, String rawLogin, String rawName, String rawEmail, String avatarUrl) {
        String email = normalizeEmail(rawEmail);
        return userRepository.findByGithubId(githubId)
                .or(() -> (email != null ? userRepository.findByEmail(email) : java.util.Optional.empty()))
                .orElseGet(() -> {
                    User u = new User();
                    u.setGithubId(githubId);
                    u.setEmail(email);
                    u.setLogin(rawLogin);
                    u.setName(rawName);
                    u.setAvatarUrl(avatarUrl);
                    // 기본 역할/상태 등 초기화 필요 시 여기서 설정
                    return userRepository.save(u);
                });
    }

    /** 공통: 이메일 정규화 */
    private String normalizeEmail(String raw) {
        if (raw == null) return null;
        String e = raw.trim();
        return e.isEmpty() ? null : e.toLowerCase();
    }
}
