package com.hyunhak.springboard.dto.member;

import lombok.Getter;

// 로그인 성공 후 JWT Access Token을 응답하기 위한 DTO
@Getter
public class TokenResponseDto {

    private final String accessToken;

    private final String refreshToken;

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
