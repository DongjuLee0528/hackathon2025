package com.example.hackathonback.review.service;

import org.springframework.stereotype.Service;

/**
 * GPT API 호출을 담당하는 서비스 클래스입니다.
 * - 향후 실제 GPT API 연동 로직이 이 클래스에 구현될 예정입니다.
 */
@Service
public class GptApiService {

    /**
     * 주어진 코드를 기반으로 GPT에게 코드 리뷰를 요청하는 메소드입니다.
     * 현재는 임시로 콘솔에 코드 내용을 출력합니다.
     *
     * @param code 사용자로부터 업로드된 전체 코드 문자열
     * @return GPT 리뷰 결과 (현재는 임시 텍스트 반환)
     */
    public String requestCodeReview(String code) {
        // TODO: 이후 OpenAI API 연동으로 대체
        System.out.println("GPT에게 리뷰 요청할 코드:\n" + code);
        return "리뷰 결과 (임시)";
    }
}
