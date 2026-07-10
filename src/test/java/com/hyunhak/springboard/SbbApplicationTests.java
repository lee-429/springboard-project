package com.hyunhak.springboard;

import com.hyunhak.springboard.dto.BoardCreateDto;
import com.hyunhak.springboard.entity.MemberEntity;
import com.hyunhak.springboard.repository.MemberRepository;
import com.hyunhak.springboard.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SbbApplicationTests {

    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testJpa() {
        for (int i = 1; i < 300; i++) {

            BoardCreateDto dto = new BoardCreateDto();

            dto.setTitle(i + "번째 제목 테스트데이터");
            dto.setContent(i + "번째 내용 테스트데이터");

            MemberEntity member = memberRepository.findById(1L).orElseThrow();



            boardService.save(dto, member);
        }
    }

}
