package com.erp.hangilse.project.controller;

import com.erp.hangilse.global.CommonResponse;
import com.erp.hangilse.project.domain.Project;
import com.erp.hangilse.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/search")
    public ResponseEntity<Page<Project>> getProjectByStatus(@RequestBody ProjectDTO.projectFilterInfo dto,
                                                            ProjectPageRequest pageRequest) {

        Page<Project> projects = projectService.getFilteringProject(null, dto, pageRequest.of());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {

        Project project = projectService.getProjectById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project);
    }

    @PostMapping
    public ResponseEntity<Project> saveProject(@RequestBody ProjectDTO.createProjectDTO dto) {

        Project project = projectService.saveProjectFromRequest(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project);
    }

    @PutMapping
    public ResponseEntity<Project> updateProject(@RequestBody ProjectDTO.updateProjectDTO dto) {

        Project project = projectService.updateProjectFromRequest(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse>deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);

        CommonResponse response = CommonResponse.builder()
                .code("Project DELETE")
                .status(200)
                .message("Project DELETE ID : "+ id)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
