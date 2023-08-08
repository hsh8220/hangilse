package com.erp.hangilse.board.service;

import com.erp.hangilse.board.controller.BoardDTO;
import com.erp.hangilse.board.domain.Board;
import com.erp.hangilse.board.domain.BoardLevel;
import com.erp.hangilse.board.domain.repository.BoardQueryRepository;
import com.erp.hangilse.board.domain.repository.BoardRepository;
import com.erp.hangilse.global.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final TagService tagService;

    public Page<Board> getBoardAll(Pageable pageable) {
        return this.boardRepository.findAll(pageable);
    }

    public Page<Board> getBoardByFilter(BoardDTO.BoardFilterInfoDTO dto, Pageable pageable) {
        return this.boardQueryRepository.findBoardDynamicQuery(dto, pageable);
    }

    public Board getBoardById(Long id) {
        return this.boardRepository.findById(id).get();
    }

    @Transactional
    public Board saveBoard(BoardDTO.createBoardDTO dto) {
        Board board = Board.builder()
                .type(dto.getType())
                .level(BoardLevel.NORMAL)
                .createTime(LocalDate.now())
                .build();

        if(dto.getLevel() != null) {
            board.setLevel(BoardLevel.valueOf(dto.getLevel()));
        }
        if(dto.getContents() != null) {
            board.setContents(dto.getContents());
        }
        if(dto.getTags() != null) {
            board.setTags(tagService.saveTags(dto.getTags()));
        }

        return this.boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(BoardDTO.updateBoardDTO dto) {
        Board board = this.getBoardById(dto.getId());
        board.setUpdateTime(LocalDate.now());

        if(dto.getType() != null) {
            board.setType(dto.getType());
        }
        if(dto.getLevel() != null) {
            board.setLevel(BoardLevel.valueOf(dto.getLevel()));
        }
        if(dto.getContents() != null) {
            board.setContents(dto.getContents());
        }
        if(dto.getTags() != null) {
            board.setTags(tagService.saveTags(dto.getTags()));
        }

        return this.boardRepository.save(board);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
