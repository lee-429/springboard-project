package com.hyunhak.springboard.service;

import com.hyunhak.springboard.dto.board.BoardCreateDto;
import com.hyunhak.springboard.dto.board.BoardResponseDto;
import com.hyunhak.springboard.dto.board.BoardUpdateDto;
import com.hyunhak.springboard.entity.BoardEntity;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.exception.BoardNotFoundException;
import com.hyunhak.springboard.exception.FileUploadException;
import com.hyunhak.springboard.exception.ForbiddenException;
import com.hyunhak.springboard.exception.LoginRequiredException;
import com.hyunhak.springboard.repository.BoardRepository;
import java.io.File;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service // 비즈니스 로직 계층이라는 걸 Spring에 알려줌 (Service 역할)
public class BoardService {

    // 생성자 주입 후 변경할 수 없도록 final 적용
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시글 저장
    public BoardEntity save(BoardCreateDto dto, MemberEntity loginMember) {

        // DTO를 BoardEntity 객체로 변환
        BoardEntity board = new BoardEntity();

        // DTO의 데이터를 BoardEntity에 복사
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            String storedFileName = saveFile(dto.getFile());

            board.setStoredFileName(storedFileName);
            board.setOriginalFileName(dto.getFile().getOriginalFilename());
        }

        // 로그인 여부 확인
        if (loginMember == null) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }

        // 로그인한 회원의 username을 작성자로 저장
        board.setWriter(loginMember.getUsername());

        // Repository에 저장
        return boardRepository.save(board);
    }

    // 전체 게시글 조회
    public Page<BoardResponseDto> findAll(Pageable pageable) {

        // Repository에서 페이지 단위로 게시글 조회
        Page<BoardEntity> boards = boardRepository.findAll(pageable);

        // BoardEntity -> BoardResponseDto 변환 후 반환
        return boards.map(board -> {
            BoardResponseDto dto = new BoardResponseDto();

            dto.setId(board.getId());
            dto.setTitle(board.getTitle());
            dto.setContent(board.getContent());
            dto.setWriter(board.getWriter());

            return dto;
        });
    }

    // 게시글 단건 조회
    public BoardResponseDto findById(Long id) {

        // Repository에서 게시글 조회
        Optional<BoardEntity> board = boardRepository.findById(id);

        // Optional에서 BoardEntity 꺼내기
        BoardEntity entity = board.orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다."));

        // BoardEntity -> BoardResponseDto 변환
        BoardResponseDto dto = new BoardResponseDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setWriter(entity.getWriter());
        dto.setStoredFileName(entity.getStoredFileName());
        dto.setOriginalFileName(entity.getOriginalFileName());

        return dto;
    }

    // 게시글 수정
    public BoardEntity update(Long id, BoardUpdateDto dto, MemberEntity loginMember) {

        // id로 기존 게시글 조회 (없으면 예외 발생)
        Optional<BoardEntity> board = boardRepository.findById(id);

        // Optional에서 실제 엔티티 꺼내기 (없으면 "게시글 없음" 예외 발생)
        BoardEntity entity = board.orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다."));

        // 로그인 여부 확인
        if (loginMember == null) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }

        // 작성자 본인인지 확인
        if (!loginMember.getUsername().equals(entity.getWriter())) {
            throw new ForbiddenException("작성자만 수정할 수 있습니다.", entity.getId());
        }

        // DTO 값을 기존 엔티티에 덮어쓰기 (수정)
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());

        // 파일 삭제 요청
        if (dto.isDeleteFile()) {

            deleteFile(entity.getStoredFileName());

            entity.setStoredFileName(null);
            entity.setOriginalFileName(null);
        }

        // 새로운 파일이 업로드된 경우
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {

            // 새 파일로 교체하기 위해 기존 파일 삭제
            deleteFile(entity.getStoredFileName());

            // 새 파일 저장
            String storedFileName = saveFile(dto.getFile());

            // DB 파일 정보 변경
            entity.setStoredFileName(storedFileName);
            entity.setOriginalFileName(dto.getFile().getOriginalFilename());
        }

        // 변경된 엔티티 저장 (JPA에서는 save가 update 역할도 함)
        return boardRepository.save(entity);
    }

    // 게시글 삭제
    public void delete(Long id, MemberEntity loginMember) {

        // id로 기존 게시글 조회 (없으면 예외 발생)
        Optional<BoardEntity> board = boardRepository.findById(id);

        // Optional에서 실제 엔티티 꺼내기 (없으면 "게시글 없음" 예외 발생)
        BoardEntity entity = board.orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다."));

        // 로그인 여부 확인
        if (loginMember == null) {
            throw new LoginRequiredException("로그인이 필요합니다.");
        }

        // 작성자 본인인지 확인
        if (!loginMember.getUsername().equals(entity.getWriter())) {
            throw new ForbiddenException("작성자만 삭제할 수 있습니다.", entity.getId());
        }

        // 첨부파일 삭제
        deleteFile(entity.getStoredFileName());

        // 게시글 삭제
        boardRepository.delete(entity);
    }

    // 제목 검색
    public Page<BoardResponseDto> searchByTitle(String keyword, Pageable pageable) {

        // 제목에 keyword가 포함된 게시글을 조회 (페이징)
        Page<BoardEntity> boardPage = boardRepository.findByTitleContaining(keyword, pageable);

        // Entity → DTO 변환
        return boardPage.map(board -> {
            BoardResponseDto dto = new BoardResponseDto();

            dto.setId(board.getId());
            dto.setTitle(board.getTitle());
            dto.setContent(board.getContent());
            dto.setWriter(board.getWriter());

            return dto;
        });

    }

    // 작성자 검색
    public Page<BoardResponseDto> searchByWriter(String keyword, Pageable pageable) {

        // 작성자에 keyword가 포함된 게시글을 조회 (페이징)
        Page<BoardEntity> boardPage = boardRepository.findByWriterContaining(keyword, pageable);

        // Entity → DTO 변환
        return boardPage.map(board -> {
            BoardResponseDto dto = new BoardResponseDto();

            dto.setId(board.getId());
            dto.setTitle(board.getTitle());
            dto.setContent(board.getContent());
            dto.setWriter(board.getWriter());

            return dto;
        });
    }

    // 제목 + 내용 검색
    public Page<BoardResponseDto> search(String keyword, Pageable pageable) {

        // JPQL을 사용하여 제목 또는 내용에 keyword가 포함된 게시글을 조회 (페이징)
        Page<BoardEntity> boardPage = boardRepository.search(keyword, pageable);

        // Entity → DTO 변환
        return boardPage.map(board -> {
            BoardResponseDto dto = new BoardResponseDto();

            dto.setId(board.getId());
            dto.setTitle(board.getTitle());
            dto.setContent(board.getContent());
            dto.setWriter(board.getWriter());

            return dto;
        });
    }

    // 파일 저장 (BoardService 내부에서만 사용하기에 private)
    private String saveFile(MultipartFile file) {

        // 업로드된 파일이 없으면 저장하지 않음
        if (file.isEmpty()) {
            return null;
        }

        // 사용자가 업로드한 원본 파일명
        String originalFileName = file.getOriginalFilename();

        // 파일명 중복 방지를 위한 UUID 생성
        String uuid = UUID.randomUUID().toString();

        // 서버 저장 파일명(UUID + 원본 파일명)
        String storedFileName = uuid + "-" + originalFileName;

        // 프로젝트 내부 uploads 폴더 경로 생성
        String route = System.getProperty("user.dir") + "/uploads";

        // uploads 폴더 객체 생성
        File uploadDir = new File(route);

        // uploads 폴더가 없으면 자동 생성
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 실제 저장될 파일 객체 생성
        File saveFile = new File(route, storedFileName);

        // 파일을 uploads 폴더에 저장
        try {
            file.transferTo(saveFile);
        } catch (Exception e) {
            throw new FileUploadException("파일 업로드에 실패했습니다.");
        }

        // 저장된 파일명 반환
        return storedFileName;
    }

    // 저장된 파일명을 받아 실제 uploads 폴더에 있는 파일을 삭제하는 메서드
    private void deleteFile(String storedFileName) {

        // 삭제할 파일명이 없으면 삭제할 파일이 없으므로 종료
        if (storedFileName == null) {
            return;
        }

        // 프로젝트 경로의 uploads 폴더 안에서 삭제할 파일 찾기
        File file = new File(
            System.getProperty("user.dir")
                    + "/uploads/"
                    + storedFileName);

        // 해당 파일이 실제로 존재하면 삭제
        if (file.exists()) {
            file.delete();
        }
    }

}
