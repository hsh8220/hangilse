package com.erp.hangilse.project.controller;

import com.erp.hangilse.ControllerTest;
import com.erp.hangilse.account.domain.Authority;
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
    void getProjectByStatus() throws Exception {
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

        ProjectDTO.projectFilterInfo filter = new ProjectDTO.projectFilterInfo();
        filter.setStatus("WORKING");

        given(projectService.getFilteringProject(any(), any(), any())).willReturn(projectList);

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
}
