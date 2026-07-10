package com.hyunhak.springboard.repository;

import com.hyunhak.springboard.entity.BoardEntity;
import com.hyunhak.springboard.entity.CommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardId(Long boardId);
}
