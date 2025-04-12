
package com.example.hackathonback.problem.repository;

import com.example.hackathonback.problem.entity.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {
    List<CodeSubmission> findByUserId(String userId);
}
