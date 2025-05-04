package com.example.hackathonback.user.repository;

import com.example.hackathonback.user.entity.ScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ScoreHistory 엔티티를 위한 JPA 리포지토리
 * - 사용자 점수 변동 이력 조회 기능 제공
 */
public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {

    /**
     * 특정 사용자 ID의 점수 이력을 최신순으로 조회
     *
     * @param userId 사용자 ID
     * @return 최신순 점수 이력 리스트
     */
    List<ScoreHistory> findByUserIdOrderByChangedAtDesc(Long userId); // ✅ 수정된 부분
}
