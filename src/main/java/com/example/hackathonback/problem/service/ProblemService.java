package com.example.hackathonback.problem.service;

import com.example.hackathonback.problem.dto.JudgeRequestDto;
import com.example.hackathonback.problem.dto.JudgeResponseDto;
import com.example.hackathonback.problem.logic.GptJudgeLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class ProblemService {

    private final GptJudgeLogic gptJudgeLogic;

    public ProblemService(GptJudgeLogic gptJudgeLogic) {
        this.gptJudgeLogic = gptJudgeLogic;
    }

    /**
     * 사용자 코드에 대한 GPT 채점 요청 처리
     */
    public JudgeResponseDto judgeCode(JudgeRequestDto dto) throws JsonProcessingException {
        return gptJudgeLogic.sendJudgeRequest(
                dto.getProblemDescription(),
                dto.getUserCode(),
                dto.getLanguage()
        );
    }
}

