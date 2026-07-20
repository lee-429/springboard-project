package com.hyunhak.springboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // 중복 불가
    private String loginId;

    private String password;

    @Column(unique = true) // 중복 불가
    private String username;

    public MemberEntity() {}

    public MemberEntity(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }
}
