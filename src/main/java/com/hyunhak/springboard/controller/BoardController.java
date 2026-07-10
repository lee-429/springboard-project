package com.hyunhak.springboard.controller;

import com.hyunhak.springboard.dto.BoardCreateDto;
import com.hyunhak.springboard.dto.BoardResponseDto;
import com.hyunhak.springboard.dto.BoardUpdateDto;
import com.hyunhak.springboard.entity.CommentEntity;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.service.BoardService;
import com.hyunhak.springboard.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/* @RestController -> JSON 데이터 반환용 (화면 없고 데이터만 반환) */
@Controller // HTML 화면 반환용 (View로 이동, Model에 데이터 담아서 전달)
public class BoardController {

    // 생성자 주입 후 변경할 수 없도록 final 적용
    private final BoardService boardService;
    private final CommentService commentService;

    @Autowired // Spring이 자동으로 해당 타입의 객체(Bean)를 찾아서 주입해주는 기능
    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    /*
    -> /board 요청
    -> "board/list" 반환
    -> Thymeleaf가 templates/board/list.html 찾음
    -> 화면 출력
    */
    // 게시글 목록
    @GetMapping("/board")
    public String view(Model model) {

        List<BoardResponseDto> boardsList = boardService.findAll();

        model.addAttribute("boards", boardsList);

        return "board/list";
    }

    // 글 작성 페이지
    @GetMapping("/board/write")
    public String write() {
        return "board/write";
    }

    // 게시글 저장
    @PostMapping("/board")
    public String save(@Valid BoardCreateDto dto, BindingResult bindingResult, HttpSession session) {

        // 검증 실패 시 작성 페이지 반환
        if (bindingResult.hasErrors()) {
            return "board/write";
        }

        // 로그인 회원 조회
        MemberEntity loginMember = (MemberEntity) session.getAttribute("loginMember");

        // 검증 성공 시 게시글 저장
        boardService.save(dto, loginMember);

        return "redirect:/board";
    }

    // 게시글 상세
    @GetMapping("/board/{id}")
    public String findById(
        @PathVariable Long id, // @PathVariable : URL 값 (주소에 붙은 값 ex: /board/3)
        Model model) {

        // 게시글 조회
        BoardResponseDto board = boardService.findById(id);

        // 해당 게시글의 댓글 조회
        List<CommentEntity> comments = commentService.findByBoardId(id);

        model.addAttribute("board", board);
        model.addAttribute("comments", comments);

        return "board/detail";
    }

    // 수정 페이지
    @GetMapping("/board/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        BoardResponseDto board = boardService.findById(id);
        model.addAttribute("board", board);
        return "board/edit";
    }

    // 게시글 수정
    @PostMapping("/board/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid BoardUpdateDto dto,
                         BindingResult bindingResult,
                         HttpSession session) {

        // 검증 실패 시 수정 페이지 반환
        if (bindingResult.hasErrors()) {
            return "board/edit";
        }

        // 로그인 회원 조회
        MemberEntity loginMember = (MemberEntity) session.getAttribute("loginMember");

        // 검증 성공 시 게시글 수정
        boardService.update(id, dto, loginMember);

        return "redirect:/board";
    }

    // 게시글 삭제
    @PostMapping("/board/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {

        // 로그인 회원 조회
        MemberEntity loginMember = (MemberEntity) session.getAttribute("loginMember");

        // 게시글 삭제
        boardService.delete(id, loginMember);

        return "redirect:/board";
    }
}
