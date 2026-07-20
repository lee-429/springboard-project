package com.hyunhak.springboard.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 애플리케이션의 모든 Controller에서 발생하는 예외를
 * 한 곳에서 공통으로 처리하는 클래스임을 나타냄
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * BoardNotFoundException이 발생했을 때 실행되는 메서드
     *
     * @param e 발생한 예외 객체
     * @param model 에러 메시지를 화면(view)으로 전달하기 위한 객체
     * @return 보여줄 에러 페이지
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFoundException(BoardNotFoundException e, Model model) {

        // 예외 메시지를 Model에 저장하여 View에서 사용할 수 있도록 전달
        model.addAttribute("message", e.getMessage());

        // templates/error/error.html 페이지를 반환
        return "error/error";
    }

    // LoginRequiredException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(LoginRequiredException.class)
    public String handleLoginRequiredException(LoginRequiredException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error";
    }

    // ForbiddenException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(ForbiddenException.class)
    public String handleForbiddenException(ForbiddenException e, Model model) {

        model.addAttribute("message", e.getMessage());
        model.addAttribute("boardId", e.getBoardId());

        return "error/error";
    }

    // FileUploadException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(FileUploadException.class)
    public String handleFileUploadException(FileUploadException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error";
    }

    // CommentNotFoundException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException(CommentNotFoundException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error";
    }

    // DuplicateLoginIdException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(DuplicateLoginIdException.class)
    public String handleDuplicateLoginIdException(DuplicateLoginIdException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error";
    }

    // DuplicateUsernameException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(DuplicateUsernameException.class)
    public String handleDuplicateUsernameException(DuplicateUsernameException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error";
    }

    // MemberNotFoundException이 발생했을 때 실행되는 메서드
    @ExceptionHandler(MemberNotFoundException.class)
    public String handleMemberNotFoundException(MemberNotFoundException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error";
    }
}
