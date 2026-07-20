package com.hyunhak.springboard.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 회원가입을 위한 데이터를 담는 DTO
public class MemberCreateDto {

    @NotBlank(message = "ID를 입력해주세요.")
    @Size(min=8, max=12, message = "ID는 8~12자 사이로 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min=8, max=15, message = "비밀번호는 8~15자 사이로 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, max=8, message = "닉네임은 2~8자 사이로 입력해주세요.")
    private String username;
}
