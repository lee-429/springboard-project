package com.hyunhak.springboard.exception;

// 회원(로그인 아이디)을 찾을 수 없을 때 발생시키는 사용자 정의 예외
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
