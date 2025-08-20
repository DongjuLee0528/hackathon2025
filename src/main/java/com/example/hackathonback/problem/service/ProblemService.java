package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.client.GptApiClient;
import com.example.hackathonback.problem.entity.Difficulty;
import com.example.hackathonback.problem.entity.Problem;
import com.example.hackathonback.problem.entity.ProblemTag;
import com.example.hackathonback.problem.repository.ProblemRepository; // ✅ 추가
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * [한 줄 요약] GPT를 통해 코딩 문제를 자동 생성/조회하는 서비스 클래스
 *
 * 사용자 요청 정보(태그, 난이도, 언어)를 바탕으로 GPT에게 문제 생성을 요청하고,
 * 응답받은 JSON 데이터를 파싱하여 Problem 엔티티로 반환합니다.
 * 또한 저장된 문제의 본문(설명)을 problemId로 조회할 수 있습니다.
 */
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final GptApiClient gptApiClient;                 // [한 줄 요약] GPT와 통신하는 클라이언트
    private final ProblemRepository problemRepository;       // ✅ 저장된 문제 조회용
    private final ObjectMapper objectMapper = new ObjectMapper(); // [한 줄 요약] JSON 응답 파싱용 객체

    /**
     * [한 줄 요약] problemId로 저장된 문제의 본문(설명) 조회
     *
     * JudgeController → GptJudgeLogic.sendJudgeRequest(...) 에 전달할 문제 텍스트를 제공합니다.
     * description 이 비어 있을 경우, title/입출력 형식을 조합해 최소 텍스트를 반환합니다.
     *
     * @param problemId 문자열 ID (숫자 문자열 가정; 리포지토리 키 타입에 맞게 조정 필요)
     * @return 문제 본문 텍스트(설명)
     */
    public String getProblemText(String problemId) {
        Long id = parseId(problemId);

        Problem p = problemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다: " + problemId));

        // 우선순위: description → (fallback) title + I/O 형식
        if (p.getDescription() != null && !p.getDescription().isBlank()) {
            return p.getDescription();
        }

        StringBuilder sb = new StringBuilder();
        if (p.getTitle() != null) sb.append("제목: ").append(p.getTitle()).append("\n\n");
        if (p.getInputFormat() != null) sb.append("입력 형식: ").append(p.getInputFormat()).append("\n");
        if (p.getOutputFormat() != null) sb.append("출력 형식: ").append(p.getOutputFormat()).append("\n");
        if (p.getExampleInput() != null) sb.append("\n예제 입력:\n").append(p.getExampleInput()).append("\n");
        if (p.getExampleOutput() != null) sb.append("\n예제 출력:\n").append(p.getExampleOutput()).append("\n");

        String fallback = sb.toString().trim();
        if (fallback.isEmpty()) {
            throw new IllegalStateException("문제 본문이 비어 있습니다: " + problemId);
        }
        return fallback;
    }

    private Long parseId(String problemId) {
        try {
            return Long.valueOf(problemId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("유효하지 않은 문제 ID 형식입니다: " + problemId);
        }
    }

    /**
     * [한 줄 요약] GPT API를 통해 코딩 문제 생성 → Problem 엔티티로 매핑
     *
     * @param tag 문제 주제 (예: 정렬, DFS 등)
     * @param difficulty 난이도 (예: EASY, MEDIUM, HARD 또는 초급, 중급, 고급)
     * @param language 언어 (예: JAVA, PYTHON 등)
     * @return Problem 객체
     * @throws Exception GPT 응답 파싱 실패 또는 형식 오류
     */
    public Problem generateProblem(String tag, String difficulty, String language) throws Exception {
        String prompt = buildPrompt(tag, difficulty, language); // GPT에 보낼 프롬프트 구성
        String gptResponse = gptApiClient.getGptResponse(prompt); // GPT API 호출

        JsonNode json = objectMapper.readTree(gptResponse); // JSON 파싱

        // [한 줄 요약] 파싱된 JSON 데이터를 기반으로 Problem 객체 생성
        Problem problem = new Problem();
        problem.setTitle(json.get("title").asText());
        problem.setDescription(json.get("description").asText());
        problem.setInputFormat(json.get("inputFormat").asText());
        problem.setOutputFormat(json.get("outputFormat").asText());
        problem.setExampleInput(json.get("exampleInput").asText());
        problem.setExampleOutput(json.get("exampleOutput").asText());
        problem.setDifficulty(parseDifficulty(difficulty)); // ✅ 난이도 문자열 → enum 변환
        problem.setTag(parseTag(tag));                     // ✅ 태그 문자열 → enum 변환

        return problem;
    }

    /**
     * [한 줄 요약] GPT에게 보낼 문제 생성 프롬프트 구성
     */
    public String buildPrompt(String tag, String difficulty, String language) {
        return String.format("""
다음과 같은 형식으로 코딩 문제를 만들어줘.
JSON 형식으로 응답해줘.

{
  "title": "문제 제목",
  "description": "문제 설명",
  "inputFormat": "입력 형식",
  "outputFormat": "출력 형식",
  "exampleInput": "예제 입력",
  "exampleOutput": "예제 출력"  
}

문제 주제: %s
난이도: %s
사용 언어: %s
""", tag, difficulty, language);
    }

    /**
     * [한 줄 요약] 문자열 난이도를 Difficulty enum으로 변환
     */
    private Difficulty parseDifficulty(String difficulty) {
        return switch (difficulty.trim().toLowerCase()) {
            case "easy", "초급" -> Difficulty.EASY;
            case "medium", "중급" -> Difficulty.MEDIUM;
            case "hard", "고급" -> Difficulty.HARD;
            default -> throw new IllegalArgumentException("지원하지 않는 난이도: " + difficulty);
        };
    }

    /**
     * [한 줄 요약] 문자열 태그를 ProblemTag enum으로 변환
     */
    private ProblemTag parseTag(String tag) {
        try {
            return ProblemTag.valueOf(tag.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 태그: " + tag);
        }
    }
}
