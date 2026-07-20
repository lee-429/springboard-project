package com.hyunhak.springboard.repository;

import com.hyunhak.springboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// BoardEntity를 관리하고, 기본 키 타입은 Long
// save(), findAll(), findById() 등의 메서드를 자동 제공
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // 제목에 keyword가 포함된 게시글 검색 (페이징)
    Page<BoardEntity> findByTitleContaining(String keyword, Pageable pageable);

    // 작성자에 keyword가 포함된 게시글 검색 (페이징)
    Page<BoardEntity> findByWriterContaining(String keyword, Pageable pageable);

    // 제목 또는 내용에 keyword가 포함된 게시글 검색 (JPQL 사용)
    // :keyword는 JPQL의 파라미터이며,
    // @Param("keyword")로 메서드의 keyword 매개변수와 연결된다.
    // 예) search("Spring", pageable)
    //     → LIKE '%Spring%' 형태의 쿼리가 실행된다.
    @Query("""
        SELECT b
        FROM BoardEntity b
        WHERE b.title LIKE %:keyword%
        OR b.content LIKE %:keyword%
    """)
    Page<BoardEntity> search(@Param("keyword") String keyword, Pageable pageable);
}
