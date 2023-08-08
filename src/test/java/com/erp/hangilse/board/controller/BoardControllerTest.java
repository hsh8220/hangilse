package com.erp.hangilse.board.controller;

import com.erp.hangilse.ControllerTest;
import com.erp.hangilse.board.domain.Board;
import com.erp.hangilse.board.domain.BoardLevel;
import com.erp.hangilse.board.service.BoardService;
import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardControllerTest extends ControllerTest {

    @MockBean
    BoardService boardService;

    @Test
    void getBoardByFilter() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        List<Board> boards = new ArrayList<>();

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        boards.add(Board.builder().type("test1").level(BoardLevel.NORMAL).account(account).build());
        boards.add(Board.builder().type("test2").level(BoardLevel.NORMAL).account(account).build());

        final PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createTime");
        Page<Board> boardPage = new PageImpl(boards, pageable, 10);

        BoardDTO.BoardFilterInfoDTO filter = new BoardDTO.BoardFilterInfoDTO();
        filter.setType("test");

        given(boardService.getBoardByFilter(any(), any())).willReturn(boardPage);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/board/search?page=0&size=10&direction=DESC").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBoardById() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        Board board = Board.builder().type("test1").level(BoardLevel.NORMAL).account(account).build();
        board.setId(123l);

        given(boardService.getBoardById(123l)).willReturn(board);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/board/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveBoard() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        BoardDTO.createBoardDTO dto = new BoardDTO.createBoardDTO();
        dto.setType("test");
        dto.setContents("board test");

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        Board board = Board.builder().type("test").level(BoardLevel.NORMAL).account(account).build();
        board.setId(123l);
        board.setContents("board test");

        given(boardService.saveBoard(dto)).willReturn(board);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/board").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void updateProject() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        BoardDTO.updateBoardDTO dto = new BoardDTO.updateBoardDTO();
        dto.setContents("board test1");

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();

        Board board = Board.builder().type("test").level(BoardLevel.NORMAL).account(account).build();
        board.setId(123l);
        board.setContents("board test1");

        given(boardService.updateBoard(dto)).willReturn(board);

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/board").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteBoard() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/board/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
