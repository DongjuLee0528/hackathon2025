package com.example.hackathonback.review.service;

import org.springframework.stereotype.Service;

/**
 * GPT API 호출을 담당하는 서비스 클래스입니다.
 * 실제 GPT 호출 로직은 이후 추가될 예정입니다.
 */
@Service
public class GptApiService {

    /**
     * 주어진 코드를 GPT에게 리뷰 요청하는 메소드입니다.
     * 현재는 임시로 코드 출력만 하고 있습니다.
     */
    public String requestCodeReview(String code) {
        System.out.println("GPT에게 리뷰 요청할 코드:\n" + code);
        return "리뷰 결과 (임시)";
    }
}