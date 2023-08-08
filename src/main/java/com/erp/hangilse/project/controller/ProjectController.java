package com.erp.hangilse.project.controller;

import com.erp.hangilse.account.service.AccountService;
import com.erp.hangilse.client.service.ClientService;
import com.erp.hangilse.global.CommonResponse;
import com.erp.hangilse.project.domain.Comment;
import com.erp.hangilse.project.domain.Project;
import com.erp.hangilse.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/search")
    public ResponseEntity<Page<Project>> getProjectByFilter(@RequestBody ProjectDTO.projectFilterInfoDTO dto,
                                                            ProjectPageRequest pageRequest) {

        Page<Project> projects = projectService.getFilteringProject(dto, pageRequest.of());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projects);
    }

    @PostMapping("/list")
    public ResponseEntity<List<ProjectDTO.projectListInfoDTO>> getProjectListByFilter(@RequestBody ProjectDTO.projectFilterInfoDTO dto) {

        List<ProjectDTO.projectListInfoDTO> projectList = projectService.getProjectList(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {

        Project project = projectService.getProjectById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(project);
    }

    @GetMapping("/meta")
    public ResponseEntity<ProjectDTO.createProjectMetaDTO> getProjectMeta() {

        ProjectDTO.createProjectMetaDTO meta = projectService.getCreateProjectMeta();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(meta);
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

    @PostMapping("/comment")
    public ResponseEntity<Comment> saveComment(@RequestBody ProjectDTO.createCommentDTO dto) {

        Comment comment = projectService.createComment(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(comment);
    }

    @PutMapping("/comment")
    public ResponseEntity<Comment> updateComment(@RequestBody ProjectDTO.updateCommentDTO dto) {

        Comment comment = projectService.updateComment(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(comment);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable long id) {

        projectService.deleteComment(id);

        CommonResponse response = CommonResponse.builder()
                .code("Comment DELETE")
                .status(200)
                .message("Comment DELETE ID : "+ id)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
