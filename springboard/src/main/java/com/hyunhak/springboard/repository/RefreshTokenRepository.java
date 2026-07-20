package com.hyunhak.springboard.repository;

import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.entity.RefreshTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // 해당 회원에게 저장된 기존 Refresh Token 삭제
    @Modifying
    @Query("delete from RefreshTokenEntity r where r.member = :member")
    void deleteByMember(@Param("member") MemberEntity member);

    // Refresh Token 값으로 조회
    Optional<RefreshTokenEntity> findByToken(String token);

    // Refresh Token 삭제
    void deleteByToken(String token);

}
