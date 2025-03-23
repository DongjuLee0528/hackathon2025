package com.example.hackathonback.git.repository;

import com.example.hackathonback.git.entity.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepoRepository extends JpaRepository<GitRepo, Long> {
}
