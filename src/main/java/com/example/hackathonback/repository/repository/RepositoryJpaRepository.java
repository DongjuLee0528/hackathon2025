package com.example.hackathonback.repository.repository;

import com.example.hackathonback.repository.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository 엔티티를 위한 JPA 레포지토리
 * - 저장소 정보에 대한 CRUD 기능을 제공
 */
public interface RepositoryJpaRepository extends JpaRepository<Repository, Long> {
    // 필요 시 사용자 ID 기반 검색 메서드 추가 가능
    // 예: List<Repository> findByUserId(Long userId);
}
