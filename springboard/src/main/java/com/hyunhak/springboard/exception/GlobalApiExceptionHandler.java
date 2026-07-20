package com.hyunhak.springboard.exception;

import com.hyunhak.springboard.dto.error.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalApiExceptionHandler {

    // 게시글 없음
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBoardNotFoundException(BoardNotFoundException e) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                404,
                e.getMessage()
            ));
    }

    // 댓글 없음
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                404,
                e.getMessage()
            ));
    }

    // 회원(로그인 아이디) 없음
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                404,
                e.getMessage()
            ));
    }

    // 로그인 하지 않음
    @ExceptionHandler(LoginRequiredException.class)
    public ResponseEntity<ApiErrorResponse> handleLoginRequiredException(LoginRequiredException e) {

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiErrorResponse(
                401,
                e.getMessage()
            ));
    }


    // 로그인 실패
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleLoginFailedException(LoginFailedException e) {

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiErrorResponse(
                401,
                e.getMessage()
            ));
    }

    // 수정/삭제 권한 없음
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(ForbiddenException e) {

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ApiErrorResponse(
                403,
                e.getMessage()
            ));
    }

    // 로그인 아이디 중복
    @ExceptionHandler(DuplicateLoginIdException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateLoginIdException(DuplicateLoginIdException e) {

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                409,
                e.getMessage()
            ));
    }

    // 닉네임 중복
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException e) {

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                409,
                e.getMessage()
            ));
    }

    // 파일 저장 오류
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiErrorResponse> handleFileUploadException(FileUploadException e) {

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiErrorResponse(
                500,
                e.getMessage()
            ));
    }

    // Validation 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
            .getFieldErrors()
            .get(0)
            .getDefaultMessage();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                400,
                message
            ));
    }

    // 유효하지 않은 Refresh Token 처리
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException e) {

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ApiErrorResponse(
                401,
                e.getMessage()
            ));
    }
}
