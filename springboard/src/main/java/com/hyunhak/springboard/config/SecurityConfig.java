package com.hyunhak.springboard.config;

import com.hyunhak.springboard.security.MemberDetailsService;
import com.hyunhak.springboard.security.jwt.JwtAuthenticationFilter;
import com.hyunhak.springboard.security.jwt.TokenProvider;
import java.util.List;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Spring Security + JWT 설정
@Configuration
public class SecurityConfig {


    // JWT 인증 필터 Bean 등록
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


    // CORS 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://3.34.173.97",
            "http://springboard-api.duckdns.org"
        ));

        configuration.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    // Security Filter Chain
    @Bean
    SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {


        http

            // CORS 적용
            .cors(cors ->
                cors.configurationSource(
                    corsConfigurationSource()
                )
            )


            // REST API + JWT 사용으로 CSRF 비활성화
            .csrf(csrf ->
                csrf.disable()
            )


            // JWT 방식은 Session 사용 안함
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )


            // 접근 권한 설정
            .authorizeHttpRequests(auth -> auth


                // Preflight 요청 허용
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                )
                .permitAll()


                // 회원 관련 API 전체 공개
                .requestMatchers(
                    "/api/members",
                    "/api/members/**",
                    "/uploads/**"
                )
                .permitAll()


                // 게시글 조회 공개
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/boards/**"
                )
                .permitAll()


                // 나머지는 로그인 필요
                .anyRequest()
                .authenticated()

            )


            // JWT 필터 등록
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }



    // 비밀번호 암호화
    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }



    // AuthenticationManager 등록
    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }
}