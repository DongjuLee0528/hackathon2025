package com.example.hackathonback.repository.repository;

import com.example.hackathonback.repository.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRepository extends JpaRepository<RepositoryEntity, Long> {
}
