package com.hyunhak.springboard.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j // @Slf4j: Logger(log) 객체 자동 생성

@Component // @Component: Spring이 관리하는 객체(Bean)으로 등록

// JWT 인증 시스템에서 토큰 생성, 검증, 정보 추출을 담당하는 클래스
public class TokenProvider {

    // application.properties에 설정된 secret key 불러옴
    @Value("${jwt.secret}")
    private String secretKey;

    // application.properties에 설정된 Access Token 유효시간 불러옴
    @Value("${jwt.access-expiration}")
    private long accessTokenExpirationTime;

    // application.properties에 설정된 Refresh Token 유효시간 불러옴
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpirationTime;

    // 키 생성
    private Key getSigningKey() {

        // Base64로 인코딩된 secret Key 텍스트를 바이트 배열로 디코딩한다.
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        // byte 배열을 기반으로 Key 객체를 만든다. (Key 객체는 서명 하거나, 서명을 검증할 때 사용)
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String createAccessToken(String loginId) {

        // 현재 시간
        Date now = new Date();

        // 현재 시간 + Access Token 유효시간
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationTime);

        // JWT 생성
        return Jwts.builder()
            // 토큰의 주체(사용자 식별값)
            .setSubject(loginId)

            // 토큰 발급 시간
            .setIssuedAt(now)

            // 토큰 만료 시간
            .setExpiration(expiryDate)

            // HS256 알고리즘과 Secret Key로 서명
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)

            // JWT 문자열 생성
            .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(String loginId) {

        // 현재 시간
        Date now = new Date();

        // 현재 시간 + Refresh Token 유효시간
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationTime);

        // JWT 생성
        return Jwts.builder()
            // 토큰의 주체(사용자 식별값)
            .setSubject(loginId)

            // 토큰 발급 시간
            .setIssuedAt(now)

            // 토큰 만료 시간
            .setExpiration(expiryDate)

            // HS256 알고리즘과 Secret Key로 서명
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)

            // JWT 문자열 생성
            .compact();
    }

    // 토큰에서 사용자 ID 추출 (Access, Refresh 동일하게 사용)
    public String getLoginIdFromToken(String token) {
        return Jwts.parserBuilder()

            // JWT 서명 검증에 사용할 Secret Key 설정
            .setSigningKey(getSigningKey())

            // JWT 파서 생성
            .build()

            // JWT 파싱 및 서명 검증
            .parseClaimsJws(token)

            // JWT의 Payload(Claims) 추출
            .getBody()

            // Subject(사용자 ID) 반환
            .getSubject();
    }

    // 토큰 유효성 검증 (Access, Refresh 동일하게 사용)
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()

                // JWT 서명 검증에 사용할 Secret Key 설정
                .setSigningKey(getSigningKey())

                // JWT 파서 생성
                .build()

                // JWT 파싱 및 유효성 검증
                .parseClaimsJws(token);

            // 검증 성공
            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }
}
