package com.hyunhak.springboard.controller;

import com.hyunhak.springboard.dto.CommentCreateDto;
import com.hyunhak.springboard.dto.CommentUpdateDto;
import com.hyunhak.springboard.entity.CommentEntity;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/board/{boardId}/comments")
    public String save(@PathVariable Long boardId, @Valid CommentCreateDto dto, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "board/detail";
        }

        MemberEntity loginMember = (MemberEntity) session.getAttribute("loginMember");

        commentService.save(dto, loginMember, boardId);

        return "redirect:/board/" + boardId;
    }

    // 댓글 수정
    @PostMapping("/comments/{commentId}/edit")
    public String update(@PathVariable Long commentId, @Valid CommentUpdateDto dto, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "board/detail";
        }

        MemberEntity loginMember = (MemberEntity) session.getAttribute("loginMember");

        CommentEntity comment = commentService.update(commentId, dto, loginMember);

        return "redirect:/board/" + comment.getBoard().getId();
    }

    // 댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public String delete(@PathVariable Long commentId, HttpSession session) {

        MemberEntity loginMember = (MemberEntity) session.getAttribute("loginMember");

        CommentEntity comment = commentService.delete(commentId, loginMember);

        return "redirect:/board/" + comment.getBoard().getId();
    }
}
