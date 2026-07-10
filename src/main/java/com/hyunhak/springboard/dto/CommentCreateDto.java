package com.hyunhak.springboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 입력(create)을 위한 데이터를 담는 DTO
public class CommentCreateDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;
}
