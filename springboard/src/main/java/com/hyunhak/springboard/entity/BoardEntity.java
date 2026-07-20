package com.hyunhak.springboard.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity // 이 클래스가 DB의 테이블과 매핑되는 엔티티임을 의미
@Getter
@Setter
public class BoardEntity {

    @Id // 기본 키(pk) 지정
    @GeneratedValue(strategy= GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT를 사용하여 id를 자동 증가
    private Long id;

    private String title;

    private String content;

    private String writer;

    // 하나의 게시글에 여러 개의 댓글이 달리는 관계(1:N)
    // mappedBy = "board" : CommentEntity의 board 필드와 연결
    @OneToMany(mappedBy = "board",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    // 서버 저장 파일명
    private String storedFileName;

    // 원본 파일명
    private String originalFileName;
}
