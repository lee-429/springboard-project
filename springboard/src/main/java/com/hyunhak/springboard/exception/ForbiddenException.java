package com.hyunhak.springboard.exception;

// 로그인은 했지만 권한이 없는 사용자가 수정/삭제를 시도할 때 발생시키는 사용자 정의 예외
public class ForbiddenException extends RuntimeException {

    private final Long boardId;

    public ForbiddenException(String message, Long boardId) {
        super(message);
        this.boardId = boardId;
    }

    public long getBoardId() {
        return boardId;
    }
}
