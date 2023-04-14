package com.erp.hangilse.project.controller;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Set;

public class ProjectDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class createProjectDTO {
        @NotBlank(message = "Name is Mandatory")
        private String name;
        @NotBlank(message = "Type is Mandatory")
        private String type;
        private LocalDate targetTime;
        private Long clientId;
        private Long accountId;
        private Set<Long> watcherIds;
        private Set<String> tags;
        private Double cost;
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class updateProjectDTO {
        @NotBlank(message = "Id is Mandatory")
        private Long id;
        private String name;
        private String type;
        private String status;
        private LocalDate targetTime;
        private Long clientId;
        private Long accountId;
        private Set<Long> watcherIds;
        private Set<String> tags;
        private Double cost;
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class projectFilterInfoDTO {
        private LocalDate startDate;
        private LocalDate endDate;
        @Email(message = "not email form")
        private String email;
        private String name;
        private String status;
        private String type;
        private String accountName;
        private String clientName;
        private Set<String> tags;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class createCommentDTO {
        @NotBlank(message = "projectId is Mandatory")
        private Long projectId;
        private Long parentId;
        @NotBlank(message = "accountId is Mandatory")
        private Long accountId;
        @NotBlank(message = "contents is Mandatory")
        private String contents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class updateCommentDTO {
        @NotBlank(message = "commentId is Mandatory")
        private Long commentId;
        @NotBlank(message = "contents is Mandatory")
        private String contents;
    }
}
