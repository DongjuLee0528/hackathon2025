package com.example.hackathonback.user.repository;

import com.example.hackathonback.user.entity.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserScore 엔티티를 위한 JPA 리포지토리
 * - 사용자별 점수/등급 정보를 관리
 */
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    /**
     * 사용자 ID를 기반으로 점수 정보 조회
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 UserScore 정보 (Optional)
     */
    Optional<UserScore> findByUserId(Long userId);
}
