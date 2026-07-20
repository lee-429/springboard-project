package com.hyunhak.springboard.exception;

// 게시글을 찾을 수 없을 때 발생시키는 사용자 정의 예외
public class BoardNotFoundException extends RuntimeException {

    /**
     * 예외 메시지를 전달하는 생성자
     *
     * @param message 예외 발생 원인 메시지
     *
     */
    public BoardNotFoundException(String message) {
        super(message);
    }
}
