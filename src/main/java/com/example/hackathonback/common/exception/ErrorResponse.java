package com.example.hackathonback.common.exception;

// [한 줄 요약] 예외 발생 시 클라이언트에게 반환할 에러 응답 객체
// 이 클래스는 예외 메시지를 JSON 형태로 응답하기 위한 단순 DTO 역할을 합니다.
// 컨트롤러에서 예외를 처리할 때 일관된 응답 형식을 제공하는 데 사용됩니다.
public class ErrorResponse {

    private final String message; // [한 줄 요약] 예외 메시지 필드

    // [한 줄 요약] 생성자에서 에러 메시지를 초기화
    public ErrorResponse(String message) {
        this.message = message;
    }

    // [한 줄 요약] 에러 메시지를 반환하는 getter
    public String getMessage() {
        return message;
    }
}
