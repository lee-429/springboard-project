package com.hyunhak.springboard.dto.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 화면(HTML)에 전달할 게시글 데이터를 담는 DTO
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;

    private String originalFileName;
    private String storedFileName;

}
