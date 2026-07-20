package com.hyunhak.springboard;

import com.hyunhak.springboard.dto.board.BoardCreateDto;
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

    // 대용량 테스트 데이터 넣기
    @Test
    void testJpa() {

        MemberEntity member = memberRepository.findByLoginId("exprim22").orElseThrow();

        for (int i = 1; i <= 300; i++) {

            BoardCreateDto dto = new BoardCreateDto();

            dto.setTitle("TEST_" + i + "번째 제목 테스트데이터");
            dto.setContent("TEST_" + i + "번째 내용 테스트데이터");

            boardService.save(dto, member);
        }
    }

}
