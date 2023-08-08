package com.erp.hangilse.board.domain.repository;

import com.erp.hangilse.board.controller.BoardDTO;
import com.erp.hangilse.board.domain.Board;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.erp.hangilse.board.domain.QBoard.board;
import static com.erp.hangilse.account.domain.QAccount.account;
import static com.erp.hangilse.global.domain.QTag.tag;

@RequiredArgsConstructor
@Repository
public class BoardQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Board> findBoardDynamicQuery(BoardDTO.BoardFilterInfoDTO dto, Pageable pageable) {

        JPAQuery query = this.queryFactory.selectFrom(board);

        query.leftJoin(board.account, account).fetchJoin();

        //searching
        if(dto.getType() != null && !dto.getType().isBlank()) query.where(board.type.eq(dto.getType()));
        if(dto.getStartDate() != null && dto.getEndDate() != null) query.where(board.createTime.between(dto.getStartDate(), dto.getEndDate()));

        if(dto.getTags() != null && dto.getTags().size()>0) {
            query.innerJoin(board.tags, tag).fetchJoin().where(tag.name.in(dto.getTags()));
        }

        //paging
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        List<Board> result = query.fetch();

        return new PageImpl<>(result, pageable, result.size());
    }

}
