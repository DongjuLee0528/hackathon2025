package com.example.hackathonback.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // [한 줄 요약] 전역 예외를 처리하는 클래스
// 이 클래스는 모든 컨트롤러에서 발생하는 예외를 공통적으로 처리해줍니다.
// 예외가 발생하면 해당 예외에 맞는 핸들러 메서드가 자동으로 호출됩니다.
public class GlobalExceptionHandler {

    /**
     * [한 줄 요약] 모든 예외(Exception)를 처리하는 핸들러
     *
     * 이 메서드는 Exception.class 타입의 예외가 발생했을 때 호출됩니다.
     * 발생한 예외의 메시지를 ErrorResponse 객체로 감싸서 클라이언트에 반환합니다.
     * HTTP 상태 코드는 400(Bad Request)으로 설정됩니다.
     *
     * @param ex 발생한 예외 객체
     * @return 에러 메시지를 담은 ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .badRequest() // HTTP 400 Bad Request 응답
                .body(new ErrorResponse(ex.getMessage())); // 예외 메시지를 담은 응답 본문
    }
}
