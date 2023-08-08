package com.erp.hangilse.project.domain.repository;

import com.erp.hangilse.project.controller.ProjectDTO;
import com.erp.hangilse.project.domain.Comment;
import com.erp.hangilse.project.domain.Project;
import com.erp.hangilse.project.domain.StatusEnum;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.erp.hangilse.account.domain.QAccount.account;
import static com.erp.hangilse.client.domain.QClient.client;
import static com.erp.hangilse.global.domain.QTag.tag;
import static com.erp.hangilse.project.domain.QComment.comment;
import static com.erp.hangilse.project.domain.QProject.project;

@RequiredArgsConstructor
@Repository
public class ProjectQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<Project> findProjectDynamicQuery(ProjectDTO.projectFilterInfoDTO dto, Pageable pageable) {

        JPAQuery query = this.queryFactory.selectFrom(project);
        //N+1 회피를 위한 fetch join 적용, food 는 page 기능문제로 hibernate.default_batch_fetch_size 사이즈 변경 적용
        query.leftJoin(project.account, account).fetchJoin();
        query.leftJoin(project.client, client).fetchJoin();
        query.leftJoin(project.watchers, account).fetchJoin();
        query.leftJoin(project.comments, comment).fetchJoin();

        //searching
        if(dto.getEmail() != null && !dto.getEmail().isBlank()) query.where(project.account.email.eq(dto.getEmail()));
        if(dto.getName() != null && !dto.getName().isBlank()) query.where(project.name.contains(dto.getName()));
        if(dto.getStatus() != null && !dto.getStatus().isBlank()) query.where(project.status.eq(StatusEnum.valueOf(dto.getStatus())));
        if(dto.getType() != null && !dto.getType().isBlank()) query.where(project.type.eq(dto.getType()));
        if(dto.getStartDate() != null && dto.getEndDate() != null) query.where(project.createTime.between(dto.getStartDate(), dto.getEndDate()));
        if(dto.getAccountName() != null && !dto.getAccountName().isBlank()) query.where(account.name.eq(dto.getAccountName()));
        if(dto.getClientName() != null && !dto.getClientName().isBlank()) query.where(client.name.eq(dto.getClientName()));

        if(dto.getTags() != null && dto.getTags().size()>0) {
            query.innerJoin(project.tags, tag).fetchJoin().where(tag.name.in(dto.getTags()));
        }

        //paging
        query.offset(pageable.getOffset()).limit(pageable.getPageSize());

        List<Project> result = query.fetch();
        //comments 계층구조를 정리
        result = result.stream().map(project -> {
           List<Comment> comments = project.getComments().stream().filter(c->c.getParent()==null).collect(Collectors.toList());
           project.setComments(comments);
           return project;
        }).collect(Collectors.toList());

        return new PageImpl<>(result, pageable, result.size());
    }

    public List<ProjectDTO.projectListInfoDTO> findProjectDynamicQuery(ProjectDTO.projectFilterInfoDTO dto) {

        JPAQuery query = this.queryFactory.select(Projections.constructor(ProjectDTO.projectListInfoDTO.class,
                project.id,
                project.name,
                project.type,
                project.status,
                project.createTime,
                project.completeTime,
                project.endTime,
                project.targetTime,
                client.name,
                account.name))
                .from(project);

        //N+1 회피를 위한 fetch join 적용, food 는 page 기능문제로 hibernate.default_batch_fetch_size 사이즈 변경 적용
        query.leftJoin(project.account, account);
        query.leftJoin(project.client, client);

        //searching
        if(dto.getEmail() != null && !dto.getEmail().isBlank()) query.where(project.account.email.eq(dto.getEmail()));
        if(dto.getName() != null && !dto.getName().isBlank()) query.where(project.name.contains(dto.getName()));
        if(dto.getStatus() != null && !dto.getStatus().isBlank()) query.where(project.status.eq(StatusEnum.valueOf(dto.getStatus())));
        if(dto.getType() != null && !dto.getType().isBlank()) query.where(project.type.eq(dto.getType()));
        if(dto.getStartDate() != null && dto.getEndDate() != null) query.where(project.createTime.between(dto.getStartDate(), dto.getEndDate()));
        if(dto.getAccountName() != null && !dto.getAccountName().isBlank()) query.where(account.name.eq(dto.getAccountName()));
        if(dto.getClientName() != null && !dto.getClientName().isBlank()) query.where(client.name.eq(dto.getClientName()));

        if(dto.getTags() != null && dto.getTags().size()>0) {
            query.leftJoin(project.tags, tag).where(tag.name.in(dto.getTags()));
        }

        List<ProjectDTO.projectListInfoDTO> result = query.fetch();

        return result;
    }

}
