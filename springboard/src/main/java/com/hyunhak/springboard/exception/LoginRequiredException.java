package com.hyunhak.springboard.exception;

// 로그인하지 않은 사용자가 기능을 이용하려고 할 때 발생시키는 사용자 정의 예외
public class LoginRequiredException extends RuntimeException {

    public LoginRequiredException(String message) {
        super(message);
    }
}
