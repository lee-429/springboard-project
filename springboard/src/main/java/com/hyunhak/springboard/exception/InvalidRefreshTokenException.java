package com.hyunhak.springboard.exception;

// Refresh Token이 유효하지 않을 때 발생하는 사용자 정의 예외
// (토큰이 존재하지 않거나, 만료되었거나, 검증에 실패한 경우 발생)
public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

}
