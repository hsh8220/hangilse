package com.erp.hangilse.client.domain.repository;

import com.erp.hangilse.client.controller.ClientDTO;
import com.erp.hangilse.client.domain.Client;
import com.erp.hangilse.project.domain.Project;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.erp.hangilse.client.domain.QClient.client;
import static com.erp.hangilse.global.domain.QTag.tag;

@RequiredArgsConstructor
@Repository
public class ClientQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Client> findClientDynamicQuery(ClientDTO.clientFilterInfoDTO dto, Pageable pageable) {

        JPAQuery query = this.queryFactory.selectFrom(client);

        //searching
        if(dto.getName() != null && !dto.getName().isBlank()) query.where(client.name.contains(dto.getName()));
        if(dto.getType() != null && !dto.getType().isBlank()) query.where(client.type.eq(dto.getType()));
        if(dto.getStartDate() != null && dto.getEndDate() != null) query.where(client.createTime.between(dto.getStartDate(), dto.getEndDate()));
        if(dto.getAddress() != null && !dto.getAddress().isBlank()) query.where(client.address.contains(dto.getAddress()));

        if(dto.getTags() != null && dto.getTags().size()>0) {
            query.innerJoin(client.tags, tag).fetchJoin().where(tag.name.in(dto.getTags()));
        }

        //paging
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        List<Client> result = query.fetch();

        return new PageImpl<>(result, pageable, result.size());
    }

}
