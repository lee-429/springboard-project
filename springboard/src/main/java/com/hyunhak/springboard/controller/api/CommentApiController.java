package com.hyunhak.springboard.controller.api;

import com.hyunhak.springboard.dto.comment.CommentCreateDto;
import com.hyunhak.springboard.dto.comment.CommentResponseDto;
import com.hyunhak.springboard.dto.comment.CommentUpdateDto;
import com.hyunhak.springboard.entity.CommentEntity;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.security.MemberPrincipal;
import com.hyunhak.springboard.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentApiController {

    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 조회
    @GetMapping
    public List<CommentResponseDto> getComments(@PathVariable Long boardId) {
        return commentService.findByBoardId(boardId);
    }

    // 댓글 작성
    @PostMapping
    public CommentResponseDto save(
        @PathVariable Long boardId,
        @RequestBody @Valid CommentCreateDto dto,

        // Authentication 객체에서 로그인 사용자 정보를 자동으로 가져와 주입
        @AuthenticationPrincipal MemberPrincipal principal) {

        // 댓글 저장 후 저장된 댓글 Entity 반환
        CommentEntity comment = commentService.save(dto, principal.getMember(), boardId);

        // Entity를 응답용 DTO로 변환하여 반환
        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public CommentResponseDto update(
        @PathVariable Long commentId,
        @RequestBody @Valid CommentUpdateDto dto,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {

        // 댓글 수정 후 수정된 댓글 Entity 반환
        CommentEntity comment = commentService.update(commentId, dto, principal.getMember());

        // Entity를 응답용 DTO로 변환하여 반환
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long commentId,
        @AuthenticationPrincipal MemberPrincipal principal) {

        // 댓글 삭제
        commentService.delete(commentId, principal.getMember());

        // 삭제 성공 응답 반환
        return ResponseEntity.ok().build();
    }




}
