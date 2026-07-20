package com.hyunhak.springboard.config;

import com.hyunhak.springboard.security.MemberDetailsService;
import com.hyunhak.springboard.security.jwt.JwtAuthenticationFilter;
import com.hyunhak.springboard.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring 설정 클래스라는 의미
// Spring Boot가 실행될 때 이 클래스를 설정 파일로 등록함
@Configuration
public class SecurityConfig {

    // JWT 인증 필터를 Spring Bean으로 등록
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(
        TokenProvider tokenProvider,
        MemberDetailsService memberDetailsService
    ) {
        return new JwtAuthenticationFilter(
            tokenProvider,
            memberDetailsService
        );
    }

    // Security 설정
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http
            // React 요청 허용
            .cors(cors -> {})

            // React + REST API 사용으로 CSRF 비활성화
            .csrf(csrf -> csrf.disable())

            // JWT 방식에서는 서버 세션을 사용하지 않음
            // 매 요청마다 JWT를 검증해서 인증 처리
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // URL별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth

                // 회원가입, 로그인, 로그아웃, 파일 다운로드는 누구나 접근 가능
                .requestMatchers(
                    "/api/members",
                    "/api/members/login",
                    "/api/members/logout",
                    "/api/members/reissue",
                    "/uploads/**")
                .permitAll()

                // 게시글 조회(GET)는 누구나 가능
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/boards/**"
                )
                .permitAll()

                // 나머지 요청은 로그인 필요
                .anyRequest()
                .authenticated()
            )

            // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            // 요청이 들어오면 JWT 검증부터 실행
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        // 설정한 SecurityFilterChain 반환
        return http.build();
    }

    // 비밀번호 암호화 기능을 제공하는 객체 생성
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 인증 처리를 담당하는 AuthenticationManager 등록
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
