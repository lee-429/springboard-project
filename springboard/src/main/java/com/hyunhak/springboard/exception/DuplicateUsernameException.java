package com.hyunhak.springboard.exception;

// 이미 사용 중인 닉네임일 때 발생시키는 사용자 정의 예외
public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException(String message) {
        super(message);
    }
}
