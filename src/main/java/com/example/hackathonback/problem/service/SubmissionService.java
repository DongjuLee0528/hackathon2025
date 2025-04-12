
package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.repository.CodeSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    private final CodeSubmissionRepository codeSubmissionRepository;

    public SubmissionService(CodeSubmissionRepository codeSubmissionRepository) {
        this.codeSubmissionRepository = codeSubmissionRepository;
    }

    public List<CodeSubmission> getSubmissionsByUserId(String userId) {
        return codeSubmissionRepository.findByUserId(userId);
    }
}
