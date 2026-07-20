package com.hyunhak.springboard.controller.api;

import com.hyunhak.springboard.dto.member.LoginDto;
import com.hyunhak.springboard.dto.member.MemberCreateDto;
import com.hyunhak.springboard.dto.member.MemberResponseDto;
import com.hyunhak.springboard.dto.member.RefreshTokenRequestDto;
import com.hyunhak.springboard.dto.member.TokenResponseDto;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.repository.RefreshTokenRepository;
import com.hyunhak.springboard.security.MemberPrincipal;
import com.hyunhak.springboard.security.jwt.TokenProvider;
import com.hyunhak.springboard.service.MemberService;
import com.hyunhak.springboard.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;
    private final TokenService tokenService;


    public MemberApiController(
        MemberService memberService,
        TokenService tokenService
    ) {

        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    // 회원가입
    @PostMapping
    public MemberResponseDto join(@RequestBody @Valid MemberCreateDto dto) {

        MemberEntity member = memberService.join(dto);

        return new MemberResponseDto(member);
    }

    // 로그인
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody @Valid LoginDto dto) {

        // 로그인 인증 및 회원 조회
        MemberEntity member = memberService.login(dto);

        // Access Token + Refresh Token 생성 및 저장
        return tokenService.createToken(member);
    }

    // 로그아웃 시 저장된 Refresh Token을 삭제하여 재발급 방지
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @RequestBody RefreshTokenRequestDto dto) {

        // 전달받은 Refresh Token 삭제 요청
        tokenService.logout(dto.getRefreshToken());

        // 로그아웃 처리 완료 응답
        return ResponseEntity.ok().build();
    }

    // 로그인한 회원 정보 반환
    @GetMapping("/me")
    public MemberResponseDto getMyInfo(
        // Spring Security에서 인증된 로그인 사용자 정보
        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        // MemberPrincipal에서 회원 엔티티 추출
        MemberEntity member = memberPrincipal.getMember();

        // 회원 정보를 DTO로 변환하여 반환
        return new MemberResponseDto(member);

    }

    // Access Token 재발급
    @PostMapping("/reissue")
    public TokenResponseDto reissue(@RequestBody RefreshTokenRequestDto dto) {

        // Refresh Token 검증 후 Access Token 재발급
        return tokenService.reissue(dto.getRefreshToken());
    }
}
