package com.hyunhak.springboard.dto.member;

import com.hyunhak.springboard.entity.MemberEntity;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;

    private String loginId;

    private String username;

    public MemberResponseDto(MemberEntity member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.username = member.getUsername();
    }
}
