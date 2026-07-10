package com.hyunhak.springboard.dto;

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
}
