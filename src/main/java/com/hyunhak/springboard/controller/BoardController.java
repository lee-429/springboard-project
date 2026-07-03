package com.hyunhak.springboard.controller;

import com.hyunhak.springboard.dto.BoardCreateDto;
import com.hyunhak.springboard.dto.BoardResponseDto;
import com.hyunhak.springboard.dto.BoardUpdateDto;
import com.hyunhak.springboard.service.BoardService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/* @RestController -> JSON 데이터 반환용 (화면 없고 데이터만 반환) */
@Controller // HTML 화면 반환용 (View로 이동, Model에 데이터 담아서 전달)
public class BoardController {

    private BoardService boardService;

    @Autowired // Spring이 자동으로 해당 타입의 객체(Bean)를 찾아서 주입해주는 기능
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
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

        ArrayList<BoardResponseDto> boardsList = boardService.findAll();

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
    public String save(BoardCreateDto dto) {

        boardService.save(dto);

        return "redirect:/board";
    }

    // 게시글 상세
    @GetMapping("/board/{id}")
    public String findById(
        @PathVariable Long id, // @PathVariable : URL 값 (주소에 붙은 값 ex: /board/3)
        Model model) {

        BoardResponseDto board = boardService.findById(id);

        model.addAttribute("board", board);

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
    public String update(@PathVariable Long id, BoardUpdateDto dto) {

        boardService.update(id, dto);

        return "redirect:/board";
    }

    // 게시글 삭제
    @PostMapping("/board/delete/{id}")
    public String delete(@PathVariable Long id) {

        boardService.delete(id);

        return "redirect:/board";
    }
}
