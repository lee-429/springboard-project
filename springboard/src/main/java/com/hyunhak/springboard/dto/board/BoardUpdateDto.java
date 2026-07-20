package com.hyunhak.springboard.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
// 수정(update)을 위한 데이터를 담는 DTO
public class BoardUpdateDto {

    // 공백 및 빈 문자열 입력 불가
    @NotBlank(message = "제목을 입력해주세요.")
    // 2~30자까지 입력 가능
    @Size(min=2, max=30, message = "제목은 2~30자 사이로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min=2, max=1000, message = "내용은 2~1000자 사이로 입력해주세요.")
    private String content;

    private MultipartFile file;

    // 기존 첨부파일 삭제 여부
    private boolean deleteFile;
}
