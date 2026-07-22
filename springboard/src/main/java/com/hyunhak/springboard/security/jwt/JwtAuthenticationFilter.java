package com.hyunhak.springboard.security.jwt;

import com.hyunhak.springboard.security.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

// JWT 인증 필터
// 요청마다 Authorization 헤더의 JWT를 확인하고 인증 정보를 SecurityContext에 저장
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberDetailsService memberDetailsService;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, MemberDetailsService memberDetailsService) {
        this.tokenProvider = tokenProvider;
        this.memberDetailsService = memberDetailsService;
    }

    // 모든 요청마다 실행되는 JWT 인증 처리 로직
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 값 가져오기
        String header = request.getHeader("Authorization");

        System.out.println(
            "JWT FILTER HEADER = " + header
        );

        // Authorization 헤더가 존재하고 Bearer 토큰 형식인지 확인
        if (header != null && header.startsWith("Bearer ")) {

            // "Bearer " 문자열 제거 후 JWT 추출
            String token = header.substring(7);

            // JWT 유효성 검증
            if (!tokenProvider.validateToken(token)) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                return;
            }

            // JWT의 Subject에서 로그인 ID 추출
            String loginId = tokenProvider.getLoginIdFromToken(token);

            // 로그인 ID를 이용해 사용자 정보 조회
            UserDetails userDetails = memberDetailsService.loadUserByUsername(loginId);

            /**
             * Spring Security 인증 객체 생성
             *
             * @param principal 인증된 사용자 정보
             * @param credentials 비밀번호 (JWT 인증에서는 사용 안함)
             * @param authorities 사용자의 권한 정보
             */
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

            // SecurityContext에 인증 정보 저장
            // 이후 Controller에서 @AuthenticationPrincipal 사용 가능
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 현재 필터 작업 완료 후 다음 필터 실행
        filterChain.doFilter(request, response);
    }
}
