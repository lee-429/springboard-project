package com.hyunhak.springboard.controller.web;

import com.hyunhak.springboard.dto.member.LoginDto;
import com.hyunhak.springboard.dto.member.MemberCreateDto;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller// HTML 화면 반환용 (View로 이동, Model에 데이터 담아서 전달)
public class MemberController {

    // 생성자 주입 후 변경할 수 없도록 final 적용
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입 페이지 이동
    @GetMapping("/member/join")
    public String joinView(Model model) {

        MemberCreateDto memberCreateDto = new MemberCreateDto();
        model.addAttribute("memberCreateDto", memberCreateDto);

        return "member/join";
    }

    // 회원가입
    @PostMapping("/member/join")
    public String join(@Valid MemberCreateDto dto, BindingResult bindingResult, Model model) {

        // Validation 오류 확인
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        try {
            memberService.join(dto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }

        return "redirect:/member/login";
    }

    // 로그인 페이지 이동
    @GetMapping("/member/login")
    public String loginView(Model model) {

        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);

        return "member/login";
    }

    // 로그인
    @PostMapping("/member/login")
    public String login(@Valid LoginDto dto, BindingResult bindingResult, HttpSession session) {

        // Validation 오류 확인
        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        // 로그인 검증 후 회원 정보 반환
        MemberEntity loginMember = memberService.login(dto);

        if (loginMember == null) {
            return "member/login";
        }

        // 로그인 정보 세션 저장
        session.setAttribute("loginMember", loginMember);

        return "redirect:/board";
    }

    // 로그아웃
    @PostMapping("/member/logout")
    public String logout(HttpServletRequest request) {

        // 기존 세션 가져오기 (없으면 생성하지 않음)
        HttpSession session = request.getSession(false);

        // 세션이 존재하면 로그인 정보 삭제
        if (session != null) {
            session.invalidate();
        }

        // 메인 페이지 이동
        return "redirect:/board";
    }
}
