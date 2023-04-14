package com.erp.hangilse.project.controller;

import com.erp.hangilse.ControllerTest;
import com.erp.hangilse.account.domain.Account;
import com.erp.hangilse.account.domain.Authority;
import com.erp.hangilse.project.domain.Comment;
import com.erp.hangilse.project.domain.Project;
import com.erp.hangilse.project.domain.StatusEnum;
import com.erp.hangilse.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectControllerTest extends ControllerTest {

    @MockBean
    ProjectService projectService;

    @Test
    void getProjectByFilter() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        List<Project> projects = new ArrayList<>();

        projects.add(Project.builder().name("Project1").type("Test").status(StatusEnum.WORKING).createTime(LocalDate.now()).build());
        projects.add(Project.builder().name("Project2").type("Test").status(StatusEnum.WORKING).createTime(LocalDate.now()).build());

        final PageRequest pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createTime");
        Page<Project> projectList = new PageImpl(projects, pageable, 10);

        ProjectDTO.projectFilterInfoDTO filter = new ProjectDTO.projectFilterInfoDTO();
        filter.setStatus("WORKING");

        given(projectService.getFilteringProject(any(), any())).willReturn(projectList);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/project/search?page=0&size=10&direction=DESC").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filter)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getProjectById() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Project project = Project.builder().name("Project1").type("Test1").status(StatusEnum.TODO).build();

        given(projectService.getProjectById(123l)).willReturn(project);

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/project/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveProject() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        ProjectDTO.createProjectDTO dto = new ProjectDTO.createProjectDTO();
        dto.setName("Project1");
        dto.setType("Test1");

        Project project = Project.builder().name("Project1").type("Test1").status(StatusEnum.TODO).build();

        given(projectService.saveProjectFromRequest(dto)).willReturn(project);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/project").headers(httpHeaders)
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

        ProjectDTO.updateProjectDTO dto = new ProjectDTO.updateProjectDTO();
        dto.setId(123l);
        dto.setName("Project2");
        dto.setType("Test2");

        Project project = Project.builder().name("Project2").type("Test2").status(StatusEnum.TODO).build();

        given(projectService.updateProjectFromRequest(dto)).willReturn(project);

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/project").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteProject() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        Project project = Project.builder().name("Project1").type("Test1").status(StatusEnum.TODO).build();


        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/project/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveComment() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        ProjectDTO.createCommentDTO dto = new ProjectDTO.createCommentDTO();
        dto.setProjectId(123l);
        dto.setAccountId(1l);
        dto.setContents("comment test");

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();
        account.setId(1l);

        Project project = Project.builder().name("Project1").type("Test1").status(StatusEnum.TODO).build();
        project.setId(123l);

        Comment comment = Comment.builder().project(project).account(account).contents("comment test").build();

        given(projectService.createComment(dto)).willReturn(comment);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/project/comment").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateComment() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        ProjectDTO.updateCommentDTO dto = new ProjectDTO.updateCommentDTO();
        dto.setCommentId(123l);
        dto.setContents("comment test2");

        Account account = Account.builder()
                .email("test@aaa.bbb")
                .password("1234")
                .name("test")
                .authorities(Set.of(Authority.builder().authorityName("ROLE_ADMIN").build()))
                .build();
        account.setId(1l);

        Project project = Project.builder().name("Project1").type("Test1").status(StatusEnum.TODO).build();
        project.setId(123l);

        Comment comment = Comment.builder().project(project).account(account).contents("comment test2").build();

        given(projectService.updateComment(dto)).willReturn(comment);

        // when
        ResultActions result = mockMvc.perform(put("/api/v1/project/comment").headers(httpHeaders)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteComment() throws Exception {
        // given
        Set<Authority> authorities = Set.of(Authority.builder().authorityName("ROLE_ADMIN").build());

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-auth-token", makeJwtAuthToken(authorities, expiredDate));

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/project/comment/123").headers(httpHeaders));

        //then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
