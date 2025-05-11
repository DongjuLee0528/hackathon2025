package com.example.hackathonback.user.service;

import com.example.hackathonback.user.entity.User;
import com.example.hackathonback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * [한 줄 요약] 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * 현재는 이메일 기반 사용자 ID 조회 기능을 제공하며,
 * 이후 사용자 정보 수정, 탈퇴, 프로필 조회 등의 로직이 확장될 수 있습니다.
 */
@Service
@RequiredArgsConstructor // [한 줄 요약] 생성자 주입 자동 생성 (userRepository)
public class UserService {

    private final UserRepository userRepository; // [한 줄 요약] 사용자 엔티티 조회를 위한 JPA 리포지토리

    /**
     * [한 줄 요약] 이메일로 사용자 ID 조회
     *
     * 이메일을 기준으로 사용자 정보를 조회하고, 존재할 경우 사용자 ID를 반환합니다.
     * 사용자가 존재하지 않으면 예외를 발생시켜 클라이언트에 오류 전달이 가능하도록 합니다.
     *
     * @param email 사용자 이메일
     * @return 사용자 ID (Long)
     * @throws IllegalArgumentException 해당 이메일의 사용자가 없을 경우
     */
    public Long findUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다: " + email));
        return user.getId();
    }
}
