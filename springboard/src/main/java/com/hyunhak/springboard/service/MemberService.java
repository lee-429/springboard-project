package com.hyunhak.springboard.service;

import com.hyunhak.springboard.dto.member.LoginDto;
import com.hyunhak.springboard.dto.member.MemberCreateDto;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.exception.DuplicateLoginIdException;
import com.hyunhak.springboard.exception.DuplicateUsernameException;
import com.hyunhak.springboard.repository.MemberRepository;
import com.hyunhak.springboard.security.MemberPrincipal;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 비즈니스 로직 계층이라는 걸 Spring에 알려줌 (Service 역할)
public class MemberService {

    // 생성자 주입 후 변경할 수 없도록 final 적용
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public MemberService(
        MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager) {

        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;

    }

    // 회원가입
    public MemberEntity join(MemberCreateDto dto) {

        // 로그인 아이디 중복 확인
        Optional<MemberEntity> memberIdOpt = memberRepository.findByLoginId(dto.getLoginId());

        // 닉네임 중복 확인
        Optional<MemberEntity> memberUsernameOpt = memberRepository.findByUsername(dto.getUsername());

        // 이미 존재하는 아이디면 회원가입 실패
        if (memberIdOpt.isPresent()) {
            throw new DuplicateLoginIdException("이미 존재하는 아이디입니다.");
        }

        // 이미 존재하는 닉네임이면 회원가입 실패
        if (memberUsernameOpt.isPresent()) {
            throw new DuplicateUsernameException("이미 존재하는 닉네임입니다.");
        }

        // 회원가입 DTO를 MemberEntity 객체로 변환
        MemberEntity entity = new MemberEntity();

        // DTO의 회원 정보를 MemberEntity에 저장
        entity.setLoginId(dto.getLoginId());
        entity.setPassword(passwordEncoder.encode(dto.getPassword())); // 비밀번호 BCrypt 암호화 후 저장
        entity.setUsername(dto.getUsername());

        // 회원 정보를 DB에 저장
        return memberRepository.save(entity);
    }

    // 로그인
    public MemberEntity login(LoginDto dto) {

        // 로그인 인증 요청 생성
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(
                dto.getLoginId(),
                dto.getPassword()
            );

        // AuthenticationManager를 통해 사용자 인증 수행
        Authentication authentication = authenticationManager.authenticate(token);

        // 인증 성공 후 Principal에서 회원 정보 추출
        MemberPrincipal principal = (MemberPrincipal) authentication.getPrincipal();

        // 인증된 회원 정보 반환
        return principal.getMember();
    }
}
