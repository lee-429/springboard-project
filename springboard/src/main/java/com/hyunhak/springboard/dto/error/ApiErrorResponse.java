package com.hyunhak.springboard.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// REST API에서 발생한 예외 정보를 클라이언트에게 전달하기 위한 응답 DTO
public class ApiErrorResponse {

    // HTTP 상태 코드 (400, 401, 404 등)
    private int status;

    // 사용자에게 전달할 에러 메시지
    private String message;
}
