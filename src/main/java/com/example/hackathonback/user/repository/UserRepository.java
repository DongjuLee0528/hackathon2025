package com.example.hackathonback.user.repository;

import com.example.hackathonback.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티를 위한 JPA 리포지토리
 * - 사용자 기본 정보 조회 및 관리
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일을 기반으로 사용자 조회
     *
     * @param email 사용자 이메일
     * @return 해당 이메일을 가진 사용자(Optional)
     */
    Optional<User> findByEmail(String email);

    /**
     * GitHub ID 기반으로 사용자 조회
     *
     * @param githubId GitHub 고유 ID
     * @return 해당 GitHub ID를 가진 사용자(Optional)
     */
    Optional<User> findByGithubId(Long githubId);
}
