package com.hyunhak.springboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Refresh Token 값
    @Column(nullable = false) // DB에 반드시 값이 있어야 함
    private String token;

    // Refresh Token 만료 시간
    @Column(nullable = false) // DB에 반드시 값이 있어야 함
    private LocalDateTime expiration;

    // Refresh Token 소유 회원
    @OneToOne(fetch = FetchType.LAZY) // MemberEntity와 1:1 관계 설정
    @JoinColumn(name = "member_id") // DB 테이블에서 member_id 컬럼으로 회원과 연결
    private MemberEntity member;

    public RefreshTokenEntity() {}

    public RefreshTokenEntity(String token, LocalDateTime expiration, MemberEntity member) {
        this.token = token;
        this.expiration = expiration;
        this.member = member;
    }
}
