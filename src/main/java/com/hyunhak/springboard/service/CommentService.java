package com.hyunhak.springboard.service;

import com.hyunhak.springboard.dto.CommentCreateDto;
import com.hyunhak.springboard.dto.CommentUpdateDto;
import com.hyunhak.springboard.entity.BoardEntity;
import com.hyunhak.springboard.entity.CommentEntity;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.repository.BoardRepository;
import com.hyunhak.springboard.repository.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    // 댓글 저장
    public CommentEntity save(CommentCreateDto dto, MemberEntity loginMember, Long boardId) {

        // 로그인하지 않은 사용자는 댓글 작성 불가
        if (loginMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 댓글이 작성될 게시글 조회 (없으면 예외 발생)
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

        // 새로운 댓글 엔티티 생성
        CommentEntity comment = new CommentEntity();

        comment.setContent(dto.getContent());
        comment.setWriter(loginMember.getUsername());
        comment.setCreatedAt(LocalDateTime.now());

        // 댓글이 어느 게시글에 속하는지 저장
        comment.setBoard(board);

        return commentRepository.save(comment);
    }

    // 게시글 ID에 해당하는 댓글 목록 조회
    public List<CommentEntity> findByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    // 댓글 수정
    public CommentEntity update(Long commentId, CommentUpdateDto dto, MemberEntity loginMember) {

        // 로그인하지 않은 사용자는 댓글 수정 불가
        if (loginMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 수정할 댓글 조회 (없으면 예외 발생)
        CommentEntity entity = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("댓글 없음"));

        // 댓글 작성자가 아니면 수정 불가
        if (!loginMember.getUsername().equals(entity.getWriter())) {
            throw new RuntimeException("작성자만 수정 할 수 있습니다.");
        }

        entity.setContent(dto.getContent());
        entity.setUpdatedAt(LocalDateTime.now()); // 댓글 수정 시간 갱신

        return commentRepository.save(entity);
    }

    // 댓글 삭제
    public CommentEntity delete(Long commentId, MemberEntity loginMember) {

        // 로그인하지 않은 사용자는 댓글 삭제 불가
        if (loginMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // 삭제할 댓글 조회 (없으면 예외 발생)
        CommentEntity entity = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("댓글 없음"));

        // 댓글 작성자가 아니면 삭제 불가
        if (!loginMember.getUsername().equals(entity.getWriter())) {
            throw new RuntimeException("작성자만 삭제 할 수 있습니다.");
        }

        commentRepository.delete(entity);

        return entity;
    }

}
