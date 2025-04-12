package com.example.hackathonback.user.repository;

import com.example.hackathonback.user.entity.ScoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {
    List<ScoreHistory> findByUserIdOrderByChangedAtDesc(Long userId); // ✅ 수정된 부분
}
