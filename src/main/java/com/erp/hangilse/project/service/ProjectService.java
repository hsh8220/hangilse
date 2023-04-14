package com.erp.hangilse.project.service;

import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.service.AccountService;
import com.erp.hangilse.client.service.ClientService;
import com.erp.hangilse.global.service.TagService;
import com.erp.hangilse.project.controller.ProjectDTO;
import com.erp.hangilse.project.domain.Comment;
import com.erp.hangilse.project.domain.Project;
import com.erp.hangilse.project.domain.StatusEnum;
import com.erp.hangilse.project.domain.repository.CommentRepository;
import com.erp.hangilse.project.domain.repository.ProjectQueryRepository;
import com.erp.hangilse.project.domain.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final CommentRepository commentRepository;
    private final TagService tagService;
    private final AccountService accountService;
    private final ClientService clientService;


    public Page<Project> getProjectByStatus(String status, Pageable pageable) {
        return projectRepository.findByStatus(status, pageable);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).get();
    }

    public Page<Project> getFilteringProject(ProjectDTO.projectFilterInfoDTO dto, Pageable pageable) {
        return projectQueryRepository.findProjectDynamicQuery(dto, pageable);
    }
    @Transactional
    public Project saveProjectFromRequest(ProjectDTO.createProjectDTO dto) {
        Project project = Project.builder()
                .name(dto.getName())
                .type(dto.getType())
                .status(StatusEnum.TODO)
                .createTime(LocalDate.now())
                .build();

        if(dto.getAccountId() != null) {
            project.setAccount(accountService.getAccountById(dto.getAccountId()));
        }
        if(dto.getClientId() != null) {
            project.setClient(clientService.getClientById(dto.getClientId()));
        }
        if(dto.getTargetTime() != null) {
            project.setTargetTime(dto.getTargetTime());
        }
        if(dto.getWatcherIds() != null) {
            List<Account> accounts = accountService.getAccountByIds(dto.getWatcherIds());
            project.setWatchers(Set.copyOf(accounts));
        }
        if(dto.getTags() != null) {
            project.setTags(tagService.saveTags(dto.getTags()));
        }
        if(dto.getCost() != null) {
            project.setCost(dto.getCost());
        }
        if(dto.getContents() != null) {
            project.setContents(dto.getContents());
        }

        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProjectFromRequest(ProjectDTO.updateProjectDTO dto) {
        Project project = projectRepository.findById(dto.getId()).orElseThrow();

        if(dto.getName() != null) {
            project.setName(dto.getName());
        }
        if(dto.getType() != null) {
            project.setType(dto.getType());
        }
        if(dto.getStatus() != null) {
            project.setStatus(StatusEnum.valueOf(dto.getStatus()));
        }
        if(dto.getAccountId() != null) {
            project.setAccount(accountService.getAccountById(dto.getAccountId()));
        }
        if(dto.getClientId() != null) {
            project.setClient(clientService.getClientById(dto.getClientId()));
        }
        if(dto.getTargetTime() != null) {
            project.setTargetTime(dto.getTargetTime());
        }
        if(dto.getWatcherIds() != null) {
            List<Account> accounts = accountService.getAccountByIds(dto.getWatcherIds());
            project.setWatchers(Set.copyOf(accounts));
        }
        if(dto.getTags() != null) {
            project.setTags(tagService.saveTags(dto.getTags()));
        }
        if(dto.getCost() != null) {
            project.setCost(dto.getCost());
        }
        if(dto.getContents() != null) {
            project.setContents(dto.getContents());
        }

        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public Comment createComment(ProjectDTO.createCommentDTO dto) {
       Account account = accountService.getAccountById(dto.getAccountId());
       Project project = projectRepository.findById(dto.getProjectId()).get();

       Comment comment = Comment.builder().createTime(LocalDateTime.now())
               .contents(dto.getContents())
               .account(account)
               .project(project)
               .build();

        if(dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId()).get();
            comment.setParent(parent);
        }

        Comment rst = commentRepository.save(comment);

       return rst;
    }

    public Comment updateComment(ProjectDTO.updateCommentDTO dto) {
        Comment comment = commentRepository.findById(dto.getCommentId()).get();
        comment.setUpdateTime(LocalDateTime.now());
        comment.setContents(dto.getContents());

        return commentRepository.save(comment);
    }

    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }
}
