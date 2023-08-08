package com.erp.hangilse.board.controller;

import com.erp.hangilse.board.domain.Board;
import com.erp.hangilse.board.service.BoardService;
import com.erp.hangilse.global.CommonResponse;
import com.erp.hangilse.project.controller.ProjectPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/search")
    public ResponseEntity<Page<Board>> getClientByFilter(@RequestBody BoardDTO.BoardFilterInfoDTO dto,
                                                         ProjectPageRequest pageRequest) {

        Page<Board> projects = boardService.getBoardByFilter(dto, pageRequest.of());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projects);
    }
    @GetMapping
    public ResponseEntity<Page<Board>> getBoardAll(BoardPageRequest pageRequest) {

        Page<Board> boards = boardService.getBoardAll(pageRequest.of());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(boards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {

        Board board = boardService.getBoardById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(board);
    }

    @PostMapping
    public ResponseEntity<Board> saveBoard(@RequestBody BoardDTO.createBoardDTO dto) {

        Board board = boardService.saveBoard(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(board);
    }

    @PutMapping
    public ResponseEntity<Board> updateBoard(@RequestBody BoardDTO.updateBoardDTO dto) {

        Board board = boardService.updateBoard(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse>deleteBoard(@PathVariable Long id) {

        boardService.deleteBoard(id);

        CommonResponse response = CommonResponse.builder()
                .code("Board DELETE")
                .status(200)
                .message("Board DELETE ID : "+ id)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
