package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.example.hackathonback.problem.entity.CodeSubmission;
import com.example.hackathonback.problem.repository.CodeSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final CodeSubmissionRepository codeSubmissionRepository;

    // 수동 저장용
    public void save(String userId, String problemId, String code, String resultJson) {
        CodeSubmission e = new CodeSubmission();
        e.setUserId(userId);
        e.setProblemId(problemId);
        e.setCode(code);
        e.setResult(resultJson);
        e.setCodeHash(sha256(maskSecrets(code)));
        codeSubmissionRepository.save(e);
    }

    // judge/solve 이후 자동 저장(비동기 권장)
    @Async
    public void saveResult(String userId, String problemId, String sourceCode, boolean saveRaw,
                           JudgeResponseDto result) {
        CodeSubmission e = new CodeSubmission();
        e.setUserId(userId);
        e.setProblemId(problemId);
        e.setResult(toJson(result)); // 간단 직렬화(필요 시 ObjectMapper로 교체)
        String masked = maskSecrets(sourceCode);
        e.setCodeHash(sha256(masked));
        if (saveRaw) {
            e.setSavedCode(trim(masked, 200_000)); // 200KB 제한
        }
        codeSubmissionRepository.save(e);
    }

    public List<CodeSubmission> getSubmissionsByUserId(String userId) {
        return codeSubmissionRepository.findByUserId(userId);
    }

    // === 유틸 ===
    private String toJson(JudgeResponseDto r) {
        // 필요 시 Jackson으로 교체 가능
        String feedback = r.getFeedback() == null ? "" : r.getFeedback().replace("\"", "\\\"");
        String suggested = r.getSuggestedCode() == null ? "" : r.getSuggestedCode().replace("\"", "\\\"");
        return """
               {"score":%d,"feedback":"%s","suggestedCode":"%s"}
               """.formatted(r.getScore(), feedback, suggested);
    }

    private String trim(String s, int max) { return s == null ? null : (s.length() > max ? s.substring(0, max) : s); }

    private String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : d) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return null; }
    }

    private static final Pattern SECRET = Pattern.compile(
            "(?i)(api[_-]?key|secret|token|password)\\s*[:=]\\s*['\\\"][^'\\\"]+['\\\"]"
    );

    private String maskSecrets(String code) {
        if (code == null) return null;
        return SECRET.matcher(code).replaceAll("$1:\"***\"");
    }
}
