package com.hyunhak.springboard.exception;

// 댓글을 찾을 수 없을 때 발생시키는 사용자 정의 예외
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
