package com.hyunhak.springboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 정적 리소스(파일) 접근 설정 + CORS 설정
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // uploads 경로 요청을 실제 파일 저장 위치와 연결
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // /uploads/** 요청이 들어오면 프로젝트의 uploads 폴더에서 파일을 찾도록 설정
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/");
    }

    // React와 Spring Boot 서버 간 CORS 허용 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            // React 개발 서버 허용
            .allowedOrigins("http://localhost:5173")
            // 허용할 HTTP 요청 방식
            .allowedMethods(
                "GET",
                "POST",
                "PUT",
                "DELETE"
            )
            // 모든 헤더 허용
            .allowedHeaders("*");
    }


}
