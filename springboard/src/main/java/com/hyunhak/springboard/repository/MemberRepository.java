package com.hyunhak.springboard.repository;

import com.hyunhak.springboard.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// MemberEntity를 관리하고, 기본 키 타입은 Long
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 로그인 아이디로 회원 조회
    // 회원이 존재하면 MemberEntity 반환, 없으면 Optional.empty() 반환
    Optional<MemberEntity> findByLoginId(String loginId);

    // 닉네임으로 회원 조회
    Optional<MemberEntity> findByUsername(String username);
}
