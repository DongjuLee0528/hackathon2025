package com.example.hackathonback.repository.repository;

import com.example.hackathonback.repository.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RepositoryEntity를 위한 JPA 레포지토리
 * - 저장소 정보에 대한 CRUD 기능을 제공
 */
public interface RepositoryRepository extends JpaRepository<RepositoryEntity, Long> {
    // 필요 시 사용자 ID 기반 검색 메서드 추가 가능
    // 예: List<RepositoryEntity> findByUserId(Long userId);
}
