package com.erp.hangilse.board.domain.repository;

import com.erp.hangilse.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"account", "tags"})
    Page<Board> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"account", "tags"})
    Optional<Board> findById(Long id);
}
