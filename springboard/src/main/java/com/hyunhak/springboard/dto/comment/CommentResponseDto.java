package com.hyunhak.springboard.dto.comment;

import com.hyunhak.springboard.entity.CommentEntity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 화면(HTML)에 전달할 댓글 데이터를 담는 DTO
public class CommentResponseDto {
    private Long id;

    private String content;

    private String writer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CommentResponseDto(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.content = commentEntity.getContent();
        this.writer = commentEntity.getWriter();
        this.createdAt = commentEntity.getCreatedAt();
        this.updatedAt = commentEntity.getUpdatedAt();
    }
}
