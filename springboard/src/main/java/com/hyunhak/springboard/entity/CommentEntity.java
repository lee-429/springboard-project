package com.hyunhak.springboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String writer;

    private LocalDateTime createdAt; // 댓글 작성 시간

    private LocalDateTime updatedAt; // 댓글 수정 시간

    // 여러 개의 댓글(Comment)이 하나의 게시글(Board)에 속하는 관계 (N:1)
    @ManyToOne
    // Comment 테이블에 생성될 외래 키(FK) 컬럼 이름을 board_id로 지정
    @JoinColumn(name = "board_id")
    // 이 댓글이 어떤 게시글에 달린 댓글인지 저장
    private BoardEntity board;
}
