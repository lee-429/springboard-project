package com.hyunhak.springboard.security;

import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 로그인 아이디로 회원 조회
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        // DB에서 로그인 아이디로 회원 조회
        MemberEntity member = memberRepository.findByLoginId(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        // 조회한 회원 정보를 Spring Security 인증 객체로 변환
        return new MemberPrincipal(member);
    }
}
