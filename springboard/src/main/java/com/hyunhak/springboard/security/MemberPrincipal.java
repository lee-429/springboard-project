package com.hyunhak.springboard.security;

import com.hyunhak.springboard.entity.MemberEntity;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
// MemberEntity 정보를 Spring Security가 사용할 수 있도록 UserDetails 형태로 제공하는 클래스
public class MemberPrincipal implements UserDetails {

    private final MemberEntity member;

    public MemberPrincipal(MemberEntity member) {
        this.member = member;
    }

    // 로그인 아이디 반환
    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    // 비밀번호 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
