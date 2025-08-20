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
        final String email = normalizeEmail(rawEmail);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + email));
        return user.getId();
    }

    /** [선택] GitHub OAuth 정보로 사용자 찾거나 생성 */
    @Transactional
    public User findOrCreateByGithub(Long githubId, String rawLogin, String rawName, String rawEmail, String avatarUrl) {
        final String email = normalizeEmail(rawEmail);
        return userRepository.findByGithubId(githubId)
                .or(() -> (email != null ? userRepository.findByEmail(email) : java.util.Optional.empty()))
                .orElseGet(() -> {
                    User u = new User();
                    u.setGithubId(githubId);
                    u.setEmail(email);
                    u.setLogin(rawLogin);
                    u.setName(rawName);
                    u.setAvatarUrl(avatarUrl);
                    return userRepository.save(u);
                });
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User loadUserByEmail(String rawEmail) {
        final String email = normalizeEmail(rawEmail);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + email));
    }

    /** GitHub 프로필 정보로 사용자 보장(없으면 생성) 후 사용자 ID 반환 — A안 핵심 */
    @Transactional
    public Long ensureUserExistsByGithub(Long githubId, String login, String name, String rawEmail, String avatarUrl) {
        final String normalized = normalizeEmail(rawEmail);
        final String email = (normalized == null || normalized.isBlank())
                ? ((login != null && !login.isBlank()) ? login : "github-user") + "@users.noreply.github.com"
                : normalized;

        // 이메일 우선 조회 → 없으면 githubId로 조회 → 없으면 생성
        User user = userRepository.findByEmail(email)
                .or(() -> (githubId != null ? userRepository.findByGithubId(githubId) : java.util.Optional.empty()))
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setGithubId(githubId);
                    u.setLogin(login);
                    u.setName(name);
                    u.setAvatarUrl(avatarUrl);
                    return userRepository.save(u);
                });

        // 누락 필드 보정(선택)
        boolean dirty = false;
        if (user.getGithubId() == null && githubId != null) { user.setGithubId(githubId); dirty = true; }
        if (user.getLogin()    == null && login    != null) { user.setLogin(login);       dirty = true; }
        if (user.getName()     == null && name     != null) { user.setName(name);         dirty = true; }
        if (user.getAvatarUrl()== null && avatarUrl!= null) { user.setAvatarUrl(avatarUrl); dirty = true; }
        if (dirty) userRepository.save(user);

        return user.getId();
    }

    /** 공통: 이메일 정규화 */
    private String normalizeEmail(String raw) {
        if (raw == null) return null;
        String e = raw.trim();
        return e.isEmpty() ? null : e.toLowerCase();
    }
}
