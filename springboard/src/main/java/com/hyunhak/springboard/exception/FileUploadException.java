package com.hyunhak.springboard.exception;

// 파일 저장 중 오류가 발생했을 때 발생시키는 사용자 정의 예외
public class FileUploadException extends RuntimeException {

    public FileUploadException(String message) {
        super(message);
    }
}
