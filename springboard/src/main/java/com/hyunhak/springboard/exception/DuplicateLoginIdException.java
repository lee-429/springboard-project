package com.hyunhak.springboard.exception;

// 이미 사용 중인 로그인 아이디일 때 발생시키는 사용자 정의 예외
public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException(String message) {
        super(message);
    }
}
