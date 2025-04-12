package com.example.hackathonback.user.repository;

import com.example.hackathonback.user.entity.UserScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserScoreRepository extends JpaRepository<UserScore, Long> {
    Optional<UserScore> findByUserId(Long userId);
}
