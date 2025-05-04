package com.example.hackathonback.git.repository;

import com.example.hackathonback.git.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Git 리포지토리 정보를 저장/조회하기 위한 JPA 레포지토리
 * - JpaRepository<GitRepo, Long> 을 상속받아 기본 CRUD 기능 제공
 */
public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {
    // 필요 시 사용자 정의 쿼리 메소드 추가 가능 (예: findByUserId 등)
}
