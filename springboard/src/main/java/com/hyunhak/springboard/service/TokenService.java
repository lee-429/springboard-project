package com.hyunhak.springboard.service;

import com.hyunhak.springboard.dto.member.TokenResponseDto;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.entity.RefreshTokenEntity;
import com.hyunhak.springboard.exception.InvalidRefreshTokenException;
import com.hyunhak.springboard.repository.RefreshTokenRepository;
import com.hyunhak.springboard.security.jwt.TokenProvider;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public TokenService(
        RefreshTokenRepository refreshTokenRepository,
        TokenProvider tokenProvider) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
    }

    // 회원 정보를 기반으로 Access Token과 Refresh Token 생성 및 저장
    public TokenResponseDto createToken(MemberEntity member) {

        // Access Token 생성
        String accessToken = tokenProvider.createAccessToken(member.getLoginId());

        // Refresh Token 생성
        String refreshToken = tokenProvider.createRefreshToken(member.getLoginId());

        // Refresh Token 만료 시간 설정
        LocalDateTime expiration = LocalDateTime.now().plusDays(14);

        // 기존 Refresh Token 삭제 (재로그인 시 기존 토큰 교체)
        refreshTokenRepository.deleteByMember(member);

        // 새로운 Refresh Token 저장
        refreshTokenRepository.save(
            new RefreshTokenEntity(
                refreshToken,
                expiration,
                member
            )
        );

        // Access Token + Refresh Token 반환
        return new TokenResponseDto(accessToken, refreshToken);
    }

    // Access Token 재발급
    public TokenResponseDto reissue(String refreshToken) {

        // JWT 자체 검증
        if (!tokenProvider.validateToken(refreshToken)) {

            throw new InvalidRefreshTokenException("유효하지 않은 Refresh Token 입니다.");
        }

        // DB에 저장된 Refresh Token 조회
        RefreshTokenEntity savedToken =
            refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(
                    () -> new InvalidRefreshTokenException("Refresh Token이 존재하지 않습니다.")
                );

        // DB 기준 만료 시간 확인
        if (savedToken.getExpiration().isBefore(LocalDateTime.now())) {

            throw new InvalidRefreshTokenException("만료된 Refresh Token입니다.");
        }

        // Refresh Token에서 로그인 ID 추출
        String loginId = tokenProvider.getLoginIdFromToken(refreshToken);

        // 새로운 Access Token 생성
        String accessToken =
            tokenProvider.createAccessToken(loginId);

        // 새로운 Access Token + 기존 Refresh Token 반환
        return new TokenResponseDto(accessToken, refreshToken);
    }

    // 로그아웃 처리
    public void logout(String refreshToken) {

        // 저장된 Refresh Token 삭제
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
