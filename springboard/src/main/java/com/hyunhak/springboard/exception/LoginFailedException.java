package com.hyunhak.springboard.exception;

// 로그인 실패했을 때 발생하는 사용자 정의 예외
public class LoginFailedException extends RuntimeException {

    public LoginFailedException(String message) {
        super(message);
    }
}
