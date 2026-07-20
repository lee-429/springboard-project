package com.hyunhak.springboard.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {

    // 재발급 요청에 사용할 Refresh Token
    private String refreshToken;
}
